# RESTfulAssignment

SOA Course Assignment, RESTful API

Team Anonymous:
- Hengji Liu, z5043667
- Igit Soeriasaputra, z5022437 

---

Job Services:

|Method|URL|Comment|
|------|---|-------|
|GET|/posting/{id}|gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|POST|/posting|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* can only be null or empty or number 0(created)/1(open) <br> *other fields* must NOT be null <br> **BAD REQUEST** if wrong syntax <br> **CREATED** if successful <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** if successful|
|DELETE|/posting/{id}|**BAD REQUEST** if wrong syntax <br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if item already has application <br> **INTERNAL SERVER ERROR** if insert fail <br> **NO CONTENT** if successful|
