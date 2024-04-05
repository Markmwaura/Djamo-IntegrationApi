# Djamo-IntegrationApi
This is an Integration Api between a third party service and an in-house company application client

# Description
The Api will cache requests & request data already sent to the third party.This will help in providing fast responses 
and preventing potentially destructive occurrences caused by client retries.

This Api will handle request in a parallel manner to improve speed but can also be upgraded to execute in non-blocking manner.Due to the uncertain nature of third party apis, we could also add functionality whenever theres's Failure or an exception is caused by a third party api , to perform automatic retrial of failed requests.


