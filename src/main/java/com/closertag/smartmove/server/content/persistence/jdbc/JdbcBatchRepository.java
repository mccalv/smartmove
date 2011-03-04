/**
 * 
 */
package com.closertag.smartmove.server.content.persistence.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.closertag.smartmove.server.content.domain.AbstractLabelValueModel;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.TimeOccurrence;
import com.closertag.smartmove.server.content.persistence.BatchRepository;

/**
 * @author mccalv
 * 
 */
public class JdbcBatchRepository implements BatchRepository {

	private static enum VALUE_PAIR_TABLE {
		extra, cost, contact;

	}

	private static final String ITEM_TABLE = "item";
	private JdbcTemplate jdbcTemplate;
	private static final String INSERT_INTO_ITEM_TABLE = "INSERT INTO "
			+ ITEM_TABLE
			+ " (id, item_id, gid_identifier, category, website, telephone, email, creation_date, update_date)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_INTO_ITEM_TABLE = "UPDATE item 	 SET id=?, item_id=?, gid_identifier=?, category=?, website=?, telephone=?, 	       email=?, creation_date=?, update_date=? WHERE id=?";
	// GeomFromText('POINT(? ?)', 4326)
	private static final String INSERT_INTO_GPS_POSITION_TABLE = "INSERT INTO gps_position(id, item_id, latitude, longitude, address, locality, geom_point)    VALUES (?, ?, [LAT], [LON], ?, ?,GeomFromText('POINT([LON] [LAT])', 4326)  )";

	private static final String INSERT_INTO_LOCALIZED_ITEM_TABLE = "INSERT INTO localizeditem(id, item_id,  label, \"value\",locale)VALUES (?, ?, ?, ?, ?)";

	private static final String INSERT_GENERIC_VALUE_KEY_TABLE = "INSERT INTO \"[TABLE]\"(id, item_id, label, \"value\", locale)  VALUES (?, ?, ?, ?, ?);";

	private static final String INSERT_TIME_OCCURRENCIES = "INSERT INTO time_occurrence(  id, item_id, start_date, end_date, address)VALUES (?, ?, ?, ?, ?);";

	public static final String DELETE_EXPIRED_ITEMS = " DELETE FROM item WHERE item.id IN( SELECT item_id as max_date FROM time_occurrence GROUP BY item_id   HAVING max(end_date)<NOW()   ORDER BY  max(end_date) DESC)";
	public static final String SELECT_CONTENTS_WITH_NO_MP3 = "SELECT i.item_id,li.locale  FROM item i, localizeditem li WHERE i.id=li.item_id AND  li.label='Description' AND li.value<>''  AND not exists (SELECT id from localizeditem li_ WHERE li_.item_id= li.item_id AND  li_.label='Mp3' and li_.locale = li.locale)   LIMIT ?";

	// SELECT i.item_id,li.locale FROM item i, localizeditem li WHERE
	// i.id=li.item_id AND li.label='Description' AND li.value<>'' AND not
	// exists (SELECT id from localizeditem li_ WHERE li_.item_id= li.item_id
	// AND li_.label='Mp3' and li_.locale = li.locale) LIMIT 1

	// SELECT i.item_id,li.locale FROM item i, localizeditem li WHERE
	// i.id=li.item_id AND li.label='Description' AND li.value<>'' AND i.id NOT
	// IN (SELECT li_.item_id from localizeditem li_ WHERE li_.id = li.id AND
	// li.label='MP3') LIMIT 10
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.BatchRepository#updateItems(java.util.
	 * List)
	 */

	public int removeExpiredItems() {
		return jdbcTemplate.update(DELETE_EXPIRED_ITEMS);

	}

	@SuppressWarnings("unchecked")
	public void updateItems(List<Item> items, final Locale locale) {

		for (final Item item : items) {
			;
			/*
			 * Checks first if the item exitsts or not
			 */

			@SuppressWarnings({ "rawtypes" })
			List query = jdbcTemplate.query("SELECT id FROM " + ITEM_TABLE
					+ " where item_id='" + item.getItemId() + "'",
					new RowMapper() {

						public Object mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							return rs.getLong("id");
						}
					});

			final Long id = query.isEmpty() ? getNextValFromSequence()
					: (Long) query.get(0);
			if (query.isEmpty()) {
				/* new insert */

				jdbcTemplate.batchUpdate(INSERT_INTO_ITEM_TABLE,
						new BatchPreparedStatementSetter() {
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {

								ps.setLong(1, id);
								ps.setString(2, item.getItemId());
								ps.setString(3, item.getGid().getIdentifier());
								ps.setString(4, item.getCategory()
										.getCategory());
								ps.setString(5, item.getWebsite());
								ps.setString(6, item.getTelephone());
								ps.setString(7, item.getEmail());
								ps.setObject(8,
										new java.sql.Date(new Date().getTime()));
								ps.setDate(9,
										new java.sql.Date(new Date().getTime()));

							}

							public int getBatchSize() {
								return 1;
							}
						});

			} else {
				// UPDATE item SET id=?, item_id=?, gid_identifier=?,
				// category=?, website=?, telephone=?, email=?, creation_date=?,
				// update_date=? WHERE id=?
				jdbcTemplate.batchUpdate(UPDATE_INTO_ITEM_TABLE,
						new BatchPreparedStatementSetter() {
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {

								ps.setLong(1, id);
								ps.setString(2, item.getItemId());
								ps.setString(3, item.getGid().getIdentifier());
								ps.setString(4, item.getCategory()
										.getCategory());
								ps.setString(5, item.getWebsite());
								ps.setString(6, item.getTelephone());
								ps.setString(7, item.getEmail());
								ps.setDate(8,
										new java.sql.Date(new Date().getTime()));
								ps.setDate(9,
										new java.sql.Date(new Date().getTime()));
								ps.setLong(10, id);

							}

							public int getBatchSize() {
								return 1;
							}
						});

				/*
				 * Recreates and insert all
				 */
				item.setId(id);
				if (locale != null) {
					insertLocalizedItem(locale, item);
					insertGpsPositions(id, item.getGpsPositions());
					insertIntoValuePairTable(VALUE_PAIR_TABLE.contact.name(),
							id, locale, item.getContacts(locale));
					insertIntoValuePairTable(VALUE_PAIR_TABLE.cost.name(), id,
							locale, item.getCosts(locale));
					insertIntoValuePairTable(VALUE_PAIR_TABLE.extra.name(), id,
							locale, item.getExtras(locale));
					insertTimeOccurences(item);
				} else {
					insertGpsPositions(id, item.getGpsPositions());
					insertLocalizedItem(Locale.ITALIAN, item);
					insertLocalizedItem(Locale.ENGLISH, item);

				}

			}

			/*
			 * Finally we performs the final insert operation
			 */

		}

	}

	public List<Map<String, Object>> getItemsWithoutLocale(int howmany) {
		return jdbcTemplate.queryForList(SELECT_CONTENTS_WITH_NO_MP3,
				new Object[] { howmany });
	}

	private void insertLocalizedItem(final Locale locale, final Item item) {
		final Long id = item.getId();

		jdbcTemplate
				.update("DELETE FROM localizeditem WHERE item_id=? and locale=? and label<>'Mp3'",
						new Object[] { id, locale.toString() });

		final List<LocalizedItem> localizedItems = item
				.getLocalizedItems(locale);
		jdbcTemplate.batchUpdate(INSERT_INTO_LOCALIZED_ITEM_TABLE,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						LocalizedItem localizedItem = localizedItems.get(i);
						ps.setLong(1, getNextValFromSequence());
						ps.setLong(2, id);
						ps.setString(3, localizedItem.getLabel().name());
						ps.setString(4, localizedItem.getValue());
						ps.setString(5, locale.toString());

					}

					public int getBatchSize() {
						return localizedItems.size();
					}
				});
	}

	@SuppressWarnings("unused")
	private void insertLocalizedItem(final Item item) {
		final Long id = item.getId();

		jdbcTemplate.update(
				"DELETE FROM localizeditem WHERE item_id=?  and label<>'Mp3'",
				new Object[] { id });

		final List<LocalizedItem> localizedItems = item.getLocalizedItems();
		jdbcTemplate.batchUpdate(INSERT_INTO_LOCALIZED_ITEM_TABLE,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						LocalizedItem localizedItem = localizedItems.get(i);
						ps.setLong(1, getNextValFromSequence());
						ps.setLong(2, id);
						ps.setString(3, localizedItem.getLabel().name());
						ps.setString(4, localizedItem.getValue());
						ps.setString(5, localizedItem.getLocale().toString());

					}

					public int getBatchSize() {
						return localizedItems.size();
					}
				});
	}

	private void insertGpsPositions(final Long id,
			final List<GpsPosition> gpsPositions) {
		jdbcTemplate.update("DELETE FROM gps_position WHERE item_id=?",
				new Object[] { id });
		for (final GpsPosition gpsPosition : gpsPositions) {

			String sql = org.apache.commons.lang.StringUtils.replace(
					INSERT_INTO_GPS_POSITION_TABLE, "[LAT]",
					"" + gpsPosition.getLatitude());
			sql = org.apache.commons.lang.StringUtils.replace(sql, "[LON]", ""
					+ gpsPosition.getLongitude());

			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				public void setValues(PreparedStatement ps, int i)
						throws SQLException {

					ps.setLong(1, getNextValFromSequence());
					ps.setLong(2, id);

					ps.setString(3, gpsPosition.getAddress());
					ps.setString(4, gpsPosition.getLocality());

				}

				/*
				 * public void setValues(PreparedStatement ps, int i) throws
				 * SQLException { GpsPosition localizedItem =
				 * gpsPositions.get(i); ps.setLong(1, getNextValFromSequence());
				 * ps.setLong(2, id); ps.setDouble(3,
				 * localizedItem.getLatitude()); ps.setDouble(4,
				 * localizedItem.getLongitude()); ps.setString(5,
				 * localizedItem.getAddress()); ps.setString(6,
				 * localizedItem.getLocality());
				 * 
				 * 
				 * ps.setObject(7, localizedItem.getGeom_point(),;
				 */
				public int getBatchSize() {
					return 1;
				}

			});

		}
	}

	private void insertTimeOccurences(final Item item) {
		jdbcTemplate.update("DELETE FROM time_occurrence WHERE item_id=?",
				new Object[] { item.getId() });

		final List<TimeOccurrence> timeOccurrences = item.getTimeOccurrences();
		jdbcTemplate.batchUpdate(INSERT_TIME_OCCURRENCIES,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						TimeOccurrence tc = timeOccurrences.get(i);

						ps.setLong(1, getNextValFromSequence());
						ps.setLong(2, item.getId());
						ps.setTimestamp(3, new java.sql.Timestamp(tc
								.getStartDate().getTime()));
						ps.setTimestamp(4, new java.sql.Timestamp(tc
								.getStartDate().getTime()));
						ps.setString(5, tc.getAddress());

					}

					public int getBatchSize() {
						return timeOccurrences.size();
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.BatchRepository#removeItemsFromCategory
	 * (java.lang.String)
	 */
	@Override
	public int removeItemsFromCategory(String category) {
		return jdbcTemplate.update("DELETE FROM  " + ITEM_TABLE
				+ "  WHERE category=?", new Object[] { category });
	}

	private void insertIntoValuePairTable(final String table,
			final Long item_id, final Locale locale,
			final List<AbstractLabelValueModel> models) {

		jdbcTemplate.update("DELETE FROM  " + table
				+ "  WHERE item_id=? and locale=?", new Object[] { item_id,
				locale.toString() });

		String sql = org.apache.commons.lang.StringUtils.replace(
				INSERT_GENERIC_VALUE_KEY_TABLE, "[TABLE]", "" + table);

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				AbstractLabelValueModel keyValye = models.get(i);

				ps.setLong(1, getNextValFromSequence());
				ps.setLong(2, item_id);
				ps.setString(3, keyValye.getLabel());
				ps.setString(4, keyValye.getValue());
				ps.setString(5, locale.toString());

			}

			public int getBatchSize() {
				return models.size();
			}
		});
	}

	/**
	 * Returns the next val for a long sequence
	 * 
	 * @return
	 */
	private Long getNextValFromSequence() {
		return jdbcTemplate.queryForLong("select nextval('id_seq') ");

	}

	/* Spring setter */
	/**
	 * associations Spring setter
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
