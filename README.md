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
|POST|/posting|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* can only be null or empty or number 0(created)/1(open) <br> *other fields* must NOT be null <br> **BAD REQUEST** if wrong syntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|DELETE|/posting/{id}|**BAD REQUEST** if wrong syntax <br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if item already has application <br> **INTERNAL SERVER ERROR** if insert fail <br> **NO CONTENT** if successful|
|PUT|/posting/{id}|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* must be valid <br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if *status* is not valid or item already has application <br> **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|GET|/postings?keyword=?&skills=?&status=?|gives back *json* <br> param optional, no or empty param means search for all <br> *status* must be valid if given <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
