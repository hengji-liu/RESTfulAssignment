# RESTfulAssignment

SOA Course Assignment, RESTful API

Team Anonymous:
- Hengji Liu, z5043667
- Igit Soeriasaputra, z5022437 

---

Job Services:

|Method|URL|Comment|
|------|---|-------|
|GET|/posting/{id}|gives back xml/json|
|POST|/posting|accepts xml/json <br> jobId must be null or empty <br> status can only be empty or number 0(created)/1(open) <br> other fields must NOT be null|
