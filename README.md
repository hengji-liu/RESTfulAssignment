
# RESTfulAssignment

SOA Course Assignment, RESTful API

Team Anonymous:
- Hengji Liu, z5043667
- Igit Soeriasaputra, z5022437 

---

## Job Services

### posting
|Method|URL|Comment|
|------|---|-------|
|GET|/posting/{id}|gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|GET|/postings?keyword=x&status=x|gives back *json* <br> param optional, no or empty param means search for all <br> *status* must be valid if given <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|POST|/posting|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* can only be null or empty or number 0(created)/1(open) <br> *other fields* must NOT be null <br> **BAD REQUEST** if wrong syntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|PUT|/posting/{id}|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* must be valid <br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if *status* is not valid or item already has application <br> **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|DELETE|/posting/{id}|**BAD REQUEST** if wrong syntax <br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if item already has application <br> **INTERNAL SERVER ERROR** if insert fail <br> **NO CONTENT** if successful|

### appliaction
|Method|URL|Comment|
|------|---|-------|
|GET|/application/{appId}| gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|GET|/applications?jobId=x| gives back *json* <br> param optional, no or empty param means search for all <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|POST|/application|accepts *xml/json* <br> *appId* and *status* must be null or empty <br> *jobId* must be an int <br> *other fields* must NOT be null <br> associated posting must be status open<br> **BAD REQUEST** if wrong yntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|PUT|/application/{id}|accepts *xml/json* <br> *appId* must be null or empty <br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is at or beyond in-review stages <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/applications/rejected/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is not at or beyond in-review <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/applications/accpeted/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is not at or beyond in-review <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|

### review
|Method|URL|Comment|
|------|---|-------|
|POST|/review|accepts *xml/json* <br> *reviewId* must be null or empty <br> *appId* must be an int <br> *decision* must be 0 or 1 <br> *other fields* must NOT be null <br> associated posting must be status in_review<br> **BAD REQUEST** if wrong yntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|GET|/review/{rId}| gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|GET|/reviews?appId=x| gives back *json* <br> param optional, no or empty param means search for all <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|PUT|/review/{id}|accepts *xml/json* <br> *reviewId* in the payload must be null or empty <br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|

---

Polling Services:

|Method|URL|HTTP_RESPONSE|CONSUME|
|------|---|-------------|-------|
|POST|/poll|**INTERNAL SERVER ERROR** if insert fail <br> **BAD REQUEST** if any of form parameters, except for comments and finalChoice is empty <br> **CREATED** with URI in the header (location) if successful|**APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
|GET|/poll/{id}|gives back *xml/json* <br> **INTERNAL SERVER ERROR** if fetch fail <br> **NO CONTENT** if item doesn't exist <br>  **OK** with *xml/json* if successful|
|GET|/polls?queryParams|gives back *json* <br> **INTERNAL SERVER ERROR** if search fail <br> **NO CONTENT** if item doesn't exist <br> **OK** with *json* if successful| param optional, no or empty param means search for all <br> *title* <br> *description* |
|DELETE|/poll/{id}|**INTERNAL SERVER ERROR** if delete fail <br> **NOT ACCEPTABLE** if the operation violates the rule or item not found <br> **OK** if successful| 
|PUT|/poll/{id}|**INTERNAL SERVER ERROR** if update fail <br> **BAD REQUEST** if any of form parameters, except for comments is empty <br> **NO CONTENT** if item doesn't exist <br> **OK** if successful| **APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
|PUT|/poll/finalise/{id}|**INTERNAL SERVER ERROR** if finalise fail <br> **BAD REQUEST** if any of form parameters, except for comments is empty <br> **NO CONTENT** if item doesn't exist <br> **OK** if successful| **APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
