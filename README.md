# RESTfulAssignment

SOA Course Assignment, RESTful API

Team Anonymous:
- Hengji Liu, z5043667
- Igit Soeriasaputra, z5022437 

---

## Job Services

### posting
``` json
{
  "jobId": "3",
  "companyName": "Amazing Co.",
  "descriptions": "yo",
  "positionType": "full time",
  "salaryRate": "600000",
  "location": "sydney cbd",
  "status": "1"
}
```
status:<br>
created = 0<br>
open = 1<br>
in review = 2<br>
processed = 3<br>
sent invitation = 4<br>

|Method|URL|Comment|
|------|---|-------|
|GET|/postings/{id}|gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|GET|/postings?keyword=x&status=x|gives back *json* <br> param optional, no or empty param means search for all <br> *status* must be valid if given <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|POST|/postings|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* can only be null or empty or number 0(created)/1(open) <br> *other fields* must NOT be null <br> **BAD REQUEST** if wrong syntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|PUT|/postings/{id}|accepts *xml/json* <br> *jobId* must be null or empty <br> *status* must be null or empty <br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if *status* is not valid or item already has application <br> **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/postings/open/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if status move backward <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/postings/in_review/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if status move backward <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/postings/processed/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if status move backward <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/postings/sent_invitations/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if status move backward <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|DELETE|/postings/{id}|**BAD REQUEST** if wrong syntax <br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if item already has application <br> **INTERNAL SERVER ERROR** if insert fail <br> **NO CONTENT** if successful|

### appliaction

```json
{
  "status": "3",
  "appId": "1",
  "jobId": "3",
  "candidateDetails": "details here",
  "coverLetter": "this is a cover letter"
}
```
status:
received = 0<br>
in review = 1<br>
accepted = 2<br>
rejected = 3<br>

|Method|URL|Comment|
|------|---|-------|
|GET|/applications/{appId}| gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|GET|/applications| gives back *json* <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|GET|/postings/{jobId}/applications| gives back *json* <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|POST|/applications|accepts *xml/json* <br> *appId* and *status* must be null or empty <br> *jobId* must be an int <br> *other fields* must NOT be null <br> associated posting must be status open<br> **BAD REQUEST** if wrong yntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|PUT|/applications/{id}|accepts *xml/json* <br> *appId* must be null or empty <br>*status* must be null or empty<br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is at or beyond in-review stages <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/applications/in_review/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is not at or beyond in-review <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/applications/rejected/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is not at or beyond in-review <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|
|PUT|/applications/accpeted/{id}| <br>**BAD REQUEST** if wrong syntax<br> **NOT FOUND** if item doesn't exist <br> **FORBIDDEN** if application is not at or beyond in-review <br>  **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|

### review

```json
{
  "appId": "1",
  "decision": "1",
  "reviewId": "1",
  "comments": "komenz",
  "reviewerDetails": "review dtails"
}
```
decision: <br>
not recommend = 0 <br>
recommend = 1 <br>

|Method|URL|Comment|
|------|---|-------|
|POST|/reviews|accepts *xml/json* <br> *reviewId* must be null or empty <br> *appId* must be an int <br> *decision* must be 0 or 1 <br> *other fields* must NOT be null <br> associated posting must be status in_review<br> **BAD REQUEST** if wrong yntax <br> **INTERNAL SERVER ERROR** if insert fail <br> **CREATED** with URI in the header if successful|
|GET|/reviews/{rId}| gives back *xml/json* <br> **BAD REQUEST** if wrong syntax or type <br> **OK** with *xml/json* if successful <br> **NOT FOUND** if item doesn't exist|
|GET|/reviews| gives back *json* <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|GET|/applications/{appID}/reviews| gives back *json* <br> **BAD REQUEST** if wrong syntax or accept type <br> **INTERNAL SERVER ERROR** if search fail <br> **OK** with *json* if successful|
|PUT|/reviews/{id}|accepts *xml/json* <br> *reviewId* in the payload must be null or empty <br> at least one of the *other fields* is NOT null <br>**BAD REQUEST** if wrong syntax or nothing to update<br> **NOT FOUND** if item doesn't exist <br> **INTERNAL SERVER ERROR** if update fail <br> **NO CONTENT** if successful|

---

## Polling Services

|Method|URL|HTTP_RESPONSE|CONSUME|
|------|---|-------------|-------|
|POST|/poll|**INTERNAL SERVER ERROR** if insert fail <br> **BAD REQUEST** if any of form parameters, except for comments and finalChoice is empty <br> **CREATED** with URI in the header (location) if successful|**APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
|GET|/poll/{id}|gives back *xml/json* <br> **INTERNAL SERVER ERROR** if fetch fail <br> **NO CONTENT** if item doesn't exist <br>  **OK** with *xml/json* if successful|
|GET|/polls?queryParams|gives back *json* <br> **INTERNAL SERVER ERROR** if search fail <br> **NO CONTENT** if item doesn't exist <br> **OK** with *json* if successful| param optional, no or empty param means search for all <br> *title* <br> *description* |
|DELETE|/poll/{id}|**INTERNAL SERVER ERROR** if delete fail <br> **NOT ACCEPTABLE** if the operation violates the rule or item not found <br> **OK** if successful| 
|PUT|/poll/{id}|**INTERNAL SERVER ERROR** if update fail <br> **BAD REQUEST** if any of form parameters, except for comments is empty <br> **NO CONTENT** if item doesn't exist <br> **OK** if successful| **APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
|PUT|/poll/finalise/{id}|**INTERNAL SERVER ERROR** if finalise fail <br> **BAD REQUEST** if any of form parameters, except for comments is empty <br> **NO CONTENT** if item doesn't exist <br> **OK** if successful| **APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|

---

## Vote Services

|Method|URL|HTTP_RESPONSE|CONSUME|
|------|---|-------------|-------|
|POST|/vote|**INTERNAL SERVER ERROR** if insert fail <br> **BAD REQUEST** if any of form parameters is empty <br> **CREATED** with URI in the header (location) if successful|**APPLICATION_FORM_URLENCODED** <br> *participantName* <br> *chosenOption* <br> *pollId* poolId URI <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
|GET|/vote/{id}|gives back *xml/json* <br> **INTERNAL SERVER ERROR** if fetch fail <br> **NO CONTENT** if item doesn't exist <br>  **OK** with *xml/json* if successful|
|PUT|/vote/{id}|**INTERNAL SERVER ERROR** if update fail <br> **BAD REQUEST** if any of form parameters, except for comments is empty <br> **NO CONTENT** if item doesn't exist <br> **OK** if successful| **APPLICATION_FORM_URLENCODED** <br> *title* <br> *description* <br> *optionsType* either "GENERIC" or "DATE" <br> *options* date/string separated by semicolon(;) <br> *comments* can be left empty <br> *finalChoice* can be left empty|
