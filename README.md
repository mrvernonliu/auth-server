### Building
mvn clean install

docker build -t mrvernonliu/auth-server:0.0.2 .

### Endpoints 

/api/client/registrationCode

/api/client/register
- `{
	"clientName":"fakeuvic",
	"flowType": "CLIENT_CREDENTIALS",
	"tokenType": "SELF_CONTAINED_TOKEN",
	"adminEmail": "test@test.com",
	"registration_code": "00000000-0000-0000-0000-000000000000"
}`

/api/account/create-account
- `{
	"firstname": "firstname",
	"lastname": "lastname",
	"email": "test1@test.ca",
	"password": "test",
	"clientUuid": "11111111-1111-1111-1111-111111111111"
}`

/api/authentication/login.   (exchange access code with tokens)
- `{
	"email": "test1@test.ca",
	"password": "test",
	"redirectUrl": "fakeuvic.com/sso",
	"clientUuid": "11111111-1111-1111-1111-111111111111"
}`

/api/authorization/sso
- `{
	"accessCode": "W98O9LadBSd6FcojYN-wi0aovi95kB5gF8LHLFW8KSZxViogjcUFhCUOXCUdcNTjpjUd8swQ_jvsSqlWaVNp5A",
	"clientUuid": "11111111-1111-1111-1111-111111111111",
	"clientSecret": "X8HjK1YKDWrzSNwLo1l2ehoa8jhlEBr2FHIGpoueS1AsX5rLVkkI9CgQEBN3HnqbRDJbo2pROCVtBpaYfMiwUg"
}`

/api/authorization/reference-token-validation
- `{
	"referenceToken": "eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJhdXRoLnZlcm5vbmxpdS5jb20iLCJpYXQiOjE1ODY0MTk5NTYsImV4cCI6MTU4NjQzMDc1Niwic3ViIjoiNDc1YTI4MDYtNGY1OC00NmY4LWE2YWItZGZlMTc1MDNjNzNkIiwicmVmZXJlbmNlVG9rZW4iOiJlNGViZjU0NC00ZmU0LTRiNTItYjI5Ny03MmFjM2M5NjAwYWQifQ.GxX_WqT-_pcFs-2ordRb7Ky4ozQ4jdTjMocC_sqe81BFPYeCSka8D-PpRIkrnyc7smrGfqqNIoxOV8NqnMNBdg",
	"clientUuid": "11111111-1111-1111-1111-111111111111",
	"clientSecret": "dtTB9LsmNsZgyN8qEcowXYE-hj-AoP-2i9FssTe5tAUwaP0MP35O5Rje8TlottQM7JcC8DXX95iJJoSf0t4NPw"
}`

/api/authorization/logout
- `{
	"accountUuid": "22222222-2222-2222-2222-222222222222", "eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJhdXRoLnZlcm5vbmxpdS5jb20iLCJpYXQiOjE1ODY0MTk5NTYsImV4cCI6MTU4NjQzMDc1Niwic3ViIjoiNDc1YTI4MDYtNGY1OC00NmY4LWE2YWItZGZlMTc1MDNjNzNkIiwicmVmZXJlbmNlVG9rZW4iOiJlNGViZjU0NC00ZmU0LTRiNTItYjI5Ny03MmFjM2M5NjAwYWQifQ.GxX_WqT-_pcFs-2ordRb7Ky4ozQ4jdTjMocC_sqe81BFPYeCSka8D-PpRIkrnyc7smrGfqqNIoxOV8NqnMNBdg",
	"clientUuid": "ebb81ddb-4dbe-464a-88b5-0e8468066bc8",
	"clientSecret": "dtTB9LsmNsZgyN8qEcowXYE-hj-AoP-2i9FssTe5tAUwaP0MP35O5Rje8TlottQM7JcC8DXX95iJJoSf0t4NPw"
}`

### Required Environment Variables
Initialize these in your bash profile
- AUTHENTICATION_DB_USER
- AUTHENTICATION_DB_PASSWORD
- AUTHENTICATION_DB_NAME
- AUTHENTICATION_DB_HOST
- AUTHENTICATION_DB_PORT
- AUTHENTICATION_DB_SSL
- AUTH_SERVER_HOST
- AUTH_WEBAPP_ORIGIN
- AUTH_WEBAPP_DOMAIN
