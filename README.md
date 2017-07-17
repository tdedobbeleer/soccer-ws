# soccer-ws
A soccer ws application

# Openshift Wildfly setup
``` bash
rhc env-set JAVA_OPTS_EXT="-Dspring.profiles.active=openshift" --app qapi \
rhc env-set JWT_TOKEN_SECRET="secret" --app qapi \
rhc env-set CLOUDINARY_API_KEY="dummy" --app qapi \
rhc env-set CLOUDINARY_API_URL="https://dummy.com" --app qapi \
rhc env-set MAILGUN_API_KEY="dummy" --app qapi \
rhc env-set MAILGUN_API_URL="https://dummy.com" --app qapi
´´´


