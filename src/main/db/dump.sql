--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: admin_register_log; Type: TABLE; Schema: public; Owner: wimove; Tablespace: 
--

CREATE TABLE admin_register_log (
    id_admin_register_log bigint NOT NULL,
    id_users bigint NOT NULL,
    register_log_datetime timestamp without time zone NOT NULL
);


ALTER TABLE public.admin_register_log OWNER TO mccalv;

--
-- Name: admin_register_log_id_admin_register_log_seq; Type: SEQUENCE; Schema: public; Owner: wimove
--

CREATE SEQUENCE admin_register_log_id_admin_register_log_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.admin_register_log_id_admin_register_log_seq OWNER TO mccalv;

--
-- Name: admin_register_log_id_admin_register_log_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: wimove
--

ALTER SEQUENCE admin_register_log_id_admin_register_log_seq OWNED BY admin_register_log.id_admin_register_log;


--
-- Name: admin_register_log_id_admin_register_log_seq; Type: SEQUENCE SET; Schema: public; Owner: wimove
--

SELECT pg_catalog.setval('admin_register_log_id_admin_register_log_seq', 1, false);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: wimove; Tablespace: 
--

CREATE TABLE roles (
    id_roles integer NOT NULL,
    code_role character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO wimove;

--
-- Name: roles_users; Type: TABLE; Schema: public; Owner: wimove; Tablespace: 
--

CREATE TABLE roles_users (
    id_roles_users bigint DEFAULT 1 NOT NULL,
    id_roles integer NOT NULL,
    id_users integer NOT NULL
);


ALTER TABLE public.roles_users OWNER TO wimove;

--
-- Name: seq; Type: SEQUENCE; Schema: public; Owner: wimove
--

CREATE SEQUENCE seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.seq OWNER TO wimove;

--
-- Name: seq; Type: SEQUENCE SET; Schema: public; Owner: wimove
--

SELECT pg_catalog.setval('seq', 1, false);


--
-- Name: users; Type: TABLE; Schema: public; Owner: wimove; Tablespace: 
--

CREATE TABLE users (
    id_users integer NOT NULL,
    name character varying(255) NOT NULL,
    surname character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    nick character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    date_activation timestamp without time zone NOT NULL,
    last_login timestamp without time zone,
    enable bigint DEFAULT 1 NOT NULL
);


ALTER TABLE public.users OWNER TO mccalv;

--
-- Name: users_id_users_seq; Type: SEQUENCE; Schema: public; Owner: wimove
--

CREATE SEQUENCE users_id_users_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.users_id_users_seq OWNER TO mccalv;

--
-- Name: users_id_users_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: wimove
--

ALTER SEQUENCE users_id_users_seq OWNED BY users.id_users;


--
-- Name: users_id_users_seq; Type: SEQUENCE SET; Schema: public; Owner: wimove
--

SELECT pg_catalog.setval('users_id_users_seq', 1, false);


--
-- Data for Name: admin_register_log; Type: TABLE DATA; Schema: public; Owner: wimove
--

COPY admin_register_log (id_admin_register_log, id_users, register_log_datetime) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: wimove
--

COPY roles (id_roles, code_role) FROM stdin;
1	Amministratore
2	Redattore
\.


--
-- Data for Name: roles_users; Type: TABLE DATA; Schema: public; Owner: wimove
--

COPY roles_users (id_roles_users, id_roles, id_users) FROM stdin;
1	1	1
1	2	2
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: wimove
--

COPY users (id_users, name, surname, email, nick, password, date_activation, last_login, enable) FROM stdin;
2	Redattore	Redattore	factory1@mediamatika.com	redattore	e10adc3949ba59abbe56e057f20f883e	2008-06-06 16:28:53	\N	1
1	Amministratore	Amministratore	dev@mediamatika.com	admin	e10adc3949ba59abbe56e057f20f883e	2008-06-06 16:28:53	2010-01-21 19:30:58.679604	1
\.


--
-- Name: admin_register_log_pkey; Type: CONSTRAINT; Schema: public; Owner: wimove; Tablespace: 
--

ALTER TABLE ONLY admin_register_log
    ADD CONSTRAINT admin_register_log_pkey PRIMARY KEY (id_admin_register_log);


--
-- Name: roles_code_role_key; Type: CONSTRAINT; Schema: public; Owner: wimove; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_code_role_key UNIQUE (code_role);


--
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: wimove; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id_roles);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--


--
-- PostgreSQL database dump complete
--

