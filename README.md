# tiny_link_shortener

![CI](https://github.com/AlNat/tiny_link_shortener/workflows/CI/badge.svg)

Simple REST API backend application of create short links and store visits.

Java based + PostgresSQL DB.


Features
--------

* Simple REST API for creating shortlinks

* Statistics of the visits with information about user

* Stateless app for horizontal scaling

* Aggregate statistics of visits

* Metrics of applications wit external dashboard (see below)

* QR code for shortlinks


Stack
-----

* Java 17, Jetty
* SpringBoot 2 (Spring Core, Spring Data, Spring MVC)
* PostgresSQL, Hibernate, HikariCP
* Flyway for DB migration
* Swagger for API documentation
* Micrometer for metrics
* TestContainer for tests


Deploy
------

First of all you need to provide the PostgreSQL DB. Schema of application will be applied automatically.


### Build

Build with Maven

```shell
mvn clean package --file pom.xml
```

### Docker

1. Get the latest docker image from [packages](https://github.com/AlNat/HTTPStatusMocker/packages)

```shell
docker docker pull ghcr.io/alnat/tiny_link_shortener:latest
```

1. Run application with Docker (ensure that's docker containers can see DB in localhost)

```shell
docker run -d -p 3000:80 -e DB_URL='jdbc:postgresql://localhost:5432/tiny_link_shortener' \
 -e DB_USER='postgres' \
 -e DB_PASSWORD='postgres' \
 --name tiny_link_shortener ghcr.io/alnat/tiny_link_shortener:latest
```


### Kubernetes

For example in minikube should be like:
```shell
kubectl create deployment tiny_link_shortener --image=ghcr.io/alnat/tiny_link_shortener:latest
kubectl expose deployment tiny_link_shortener --type=LoadBalancer --port=80
```

or you can use base deployment in `deployment.yml` in repo


Metrics
-------

App collects metrics using Micrometer.

To collect it in your Prometheus should be defined task:

```yml
scrape_configs:
- job_name: 'tiny_url_shortner'
  metrics_path: '/actuator/prometheus'
  scrape_interval: 600s
  static_configs:
    - targets: ['localhost:88']
```

There is a default Grafana dashboard of business-level metrics (see `grafana_dashboard.json`), 
for tech metrics use default SpringBoot dashboard or any other one.


#### Metrics list


Metrics collect with default Micrometer API, so duration divides in 3 different metrics (see below) 
and a lot of default Java, Hikari, Jetty and Spring metrics.


Business metrics:

| Name                             | Description                                       | Tags                             |
|----------------------------------|---------------------------------------------------|----------------------------------|
| link_created_total               | Count of created new shortlinks                   | -                                |
| link_search_timing_seconds_sum   | Shortlink search time sum                         | -                                |
| link_search_timing_seconds_count | Shortlink search time count                       | -                                |
| link_search_timing_seconds_max   | Max of shortlink search time (reset every minute) | -                                |
| link_visit_total                 | Visit to the all shortlinks                       | result_status (status of visits) |
| handled_error_total              | REST API errors                                   | code (code in result block)      |
| qr_generated                     | Count of generated QR codes                       | -                                |


Configuration
-------------

| GROUP      | Parameter                     | Description                                                                     | Available values                     | Default                                              |
|------------|-------------------------------|---------------------------------------------------------------------------------|--------------------------------------|------------------------------------------------------|
| JVM        | SERVER_PORT                   | app port                                                                        | positive int                         | 80                                                   |
| JVM        | MANAGEMENT_SERVER_PORT        | actuator port                                                                   | positive int                         | 88                                                   |
| JVM        | HTTP_MIN_THREADS              | Minimum HTTP thread pool size                                                   | positive int                         | 8                                                    |
| JVM        | HTTP_MAX_THREADS              | Maximum HTTP thread pool size                                                   | positive int                         | 100                                                  |
| JVM        | HTTP_MAX_QUEUE                | HTTP thread-poll queue                                                          | positive int                         | 100                                                  |
| Datasource | DB_URL                        | DB URL                                                                          | JDBC URL                             | jdbc:postgresql://localhost:5432/tiny_link_shortener |
| Datasource | DB_USER                       | DB username                                                                     | string                               | postgres                                             |
| Datasource | DB_PASSWORD                   | DB password                                                                     | string                               | postgres                                             |
| Datasource | DEFAULT_TRANSACTION_TIMEOUT   | Transaction timeout                                                             | positive int                         | 30                                                   |
| Datasource | DB_HIKARI_MIN_IDLE            | DB thread pool min size                                                         | positive int                         | 4                                                    |
| Datasource | DB_HIKARI_MAX_POOL_SIZE       | DB thread pool max size                                                         | positive int                         | 4                                                    |
| Datasource | DB_HIKARI_AUTO_COMMIT         | Auto-commit flag                                                                | boolean flag                         | false                                                |
| Datasource | DB_HIKARI_IDLE_TIMEOUT        | Timeout to shrink db poll                                                       | positive int, in ms                  | 30000                                                |
| Swagger    | SWAGGER_OPERATIONS            | Available methods in SwaggerUI                                                  | list of HTTP methods separate by `,` | "get", "post", "delete"                              |
| Swagger    | ENABLE_SWAGGER                | Turn on Swagger UI in app                                                       | boolean flag                         | true                                                 |
| ThreadPool | EXECUTION_POOL_ALLOW_TIMEOUT  | Async thread poll (for long polling) allow timeout flag                         | boolean flag                         | true                                                 |
| ThreadPool | EXECUTION_POOL_CORE_SIZE      | Async thread poll (for long polling) size                                       | positive int                         | 10                                                   |
| ThreadPool | EXECUTION_POOL_MAX_SIZE       | Async thread poll (for long polling) max size                                   | positive int                         | 100                                                  |
| ThreadPool | EXECUTION_POOL_KEEP_ALIVE     | Async thread poll (for long polling) keep alive timeout                         | positive int                         | 60s                                                  |
| ThreadPool | EXECUTION_POOL_QUEUE_CAPACITY | Async thread poll (for long polling) threads in queue size                      | Java duration                        | 100                                                  |
| Custom     | BUSINESS_LOG_LEVEL            | Global log level ()                                                             | TRACE, DEBUG, INFO, WARN, ERROR      | INFO                                                 |
| Custom     | SHORT_LINK_GENERATOR          | Generators for shortlink                                                        | SEQUENCE                             | SEQUENCE                                             |
| Custom     | METRIC_PREFIX                 | Prefix for business metrics in micrometer                                       | string                               |                                                      |
| Custom     | DEFAULT_TASK_PAGING_TIMEOUT   | Timeout of paging and aggregate visits search, in seconds                       | positive int                         | 30                                                   |
| Custom     | IS_BEHIND_PROXY               | Flag of usage custom header to receive visits IP address                        | boolean flag                         | false                                                |
| Custom     | CLIENT_IP_ADDRESS_HEADER      | Custom header name of upper point                                               | string                               | X-IP-ADDRESS                                         |
| Custom.QR  | QR_HEIGHT                     | QR image height                                                                 | positive int                         | 200                                                  |
| Custom.QR  | QR_WIDTH                      | QR image width                                                                  | positive int                         | 200                                                  |
| Custom.QR  | QR_ENDPOINT                   | Full endpoint of shortlink, must have %s in place when shortlink will be placed | string                               | http://localhost:80/s/%s                             |



Development information
-----------------------

### DB Schema

<img src="/db_schema.png" alt="DB schema from repo">

See `db_schema.png` in repo and `\src\main\resources\db` with file migrations list


### API

REST API build with custom DTO Result object.

In general case HTTP response code should be 200, indicates that's request processed correctly.
In JSON response there is code field with http-synonymous code of result. 
Normally its 200, but in error case may be other.

Also in heavy search request (of visits aggregate statistics or paging visits) there is a long-polling request with thread parking.
It uses the separated thread poll to process this requests.

Swagger available at default URL (/swagger-ui/index.html#/)


### Internal short engine

#### ID Based

Receive single value of the DB sequence, convert in from 10-base digit to custom-alphabet-base digit.
Just like from 2-based to 10 based and reversal, but instead of 2 it's much more.

ALPHABET includes all symbols from `A` to `z` and `0` to `9`, plus specials `-` and `_`, 
but excluding some can be misunderstanding:
`I`, `i`, `L`, `l`, `1` and `O`, `o`, `0`

###### WARNING

`_` is like 0 in ten-based -- leading zero is not should be
so `__a` is absolute equivalent of `a`
just like `01` is equal to `1`


### Test

There are 2 kind of tests in the app:
1) The ordinal, simple unit tests. It covers engine with math, JSON DTO mapping and other complicated parts of app.
2) The mix of E2E and integration tests with testcontainers. In these tests the DB starts in Docker container, 
app initializing and starts API interaction of some use-case of app: create the link, visist it, see the stats, for example. 

The main reason of it to awoid to cover the all app of units and test the main processed of usage in almost real enviroment:
DB in container, REST API interaction step-by-step


### BaseProtoFramework

There is a simple base framework for build Service and Model, 
see `BaseService`, `BaseCRUDService` for details for service and `Model` and `Activating` for entity.
Provides some basic CRUD operation for Entity
