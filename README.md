# Extended Application Management Admin Service
A custom admin service to edit the federated identity provider list in the authentication step configurations of a 
existing service provider

In order to access the *WSDL*, use the following URL.

```
https://localhost:9443/services/ExtendedApplicationManagementAdminService?wsdl
```
# Services

The service provides following two operations.

- addIdentityProviderToApplication: Add an IDP
- removeIdentityProviderFromApplication: Remove an IDP

# Inputs

- applicationName: Name of the Service Provider 
- identityProviderName: Name of the Identity Provider
- authenticationStepId: Authentication step number
