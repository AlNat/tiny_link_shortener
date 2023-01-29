TODO LIST
=========

Phase might be equivalent of a sprint

Phase 1 - MVP part 1
--------------------

* Engine
  - ~~Shortener algorithm~~
  - ~~Status model~~
  - ~~DB SQL (2 tables: links and visits)~~

* Application
  - ~~Shortener Engine~~
  - ~~Entity classes + Enum for statuses~~
  - ~~Common stuff (Result object, Utils)~~
  - ~~Repository layer (with search on DTO with Criteria)~~
  - ~~DTO + Mappers~~
  - ~~Service layer~~
  - ~~Controller layer (Swagger + API + Validation)~~
    - ~~Links~~
    - ~~Visits (base and aggregate -- for links; paginal throw links)~~


Phase 2 - MVP part 2
--------------------

* ~~Tests~~
  - ~~Engine test (unit)~~
  - ~~UseCase tests (mix of E2E and integration test)~~

* ~~Infrastructure~~
  - ~~CI with GitHub CI~~
  - ~~Docker image~~


Phase 3 - Operations
--------------------

* ~~Metrics~~
  - ~~Prometheus and micrometer~~

* ~~Description and documentation~~
  - ~~README about app~~
  - ~~Comments in DB and table for each status type with values~~

* ~~Infrastructure~~
  - ~~Grafana UI with prometheus based storage (with json representation in file in repo)~~
  - ~~K8S deployment (test with minikub)~~


Phase 4 - Features part 1
-------

* ~~QR code generator for links~~

* Security
  - Spring security with authorized API requests
  - Add test for all above

* ~~Separate Swagger group API for private and public~~


Phase 5 - Features part 2
-------------------------

* Custom links short URL
  - Custom generator with checking with existing
  - Generator resolver
  - Stop list of some links

* Password auth for links visit (custom popup or browser default pop)

* Saves not found links in visits table


Phase 6 - Features part 3
-------------------------

* SonarKube check outside of IDEA (SonarLint) in build CI with quality gate

* Maybe some UI: not only REST API, but with vaadin\simple jsp

* Routing DS for read from replica DB, and write to primary (?)
