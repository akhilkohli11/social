/** Obtain the local shared object for the Access Enabler. */
function getAccessEnabler() {
    return ae;
}
/**
* AccessEnabler Functions
*/
/** Identify yourself to the Access Enabler. */
function setRequestor(requestor_id, sp_configuration) {
    getAccessEnabler().setRequestor(requestor_id, sp_configuration);
}
/** Ask for authorization for a given resource. */
function getAuthorization(resource_id) {
    getAccessEnabler().getAuthorization(resource_id);
}
/**
* Check authorization for a given resource.
* This does not authenticate, but queries the authorization status
* if the user is already authenticated.
*/
function checkAuthorization(resource_id) {
    getAccessEnabler().checkAuthorization(resource_id);
}
/**
* Check authentication status for the user.
* This does not authenticate, but queries the authentication status.
*/
function checkAuthentication() {
    getAccessEnabler().checkAuthentication();
}
/**
* Start the authentication flow if no valid authentication token
* is found in the local shared object.
*/
function getAuthentication() {
    getAccessEnabler().getAuthentication();
}
/**
* Clear all authentication and authorization for the client.
*/
function logout() {
    getAccessEnabler().logout();
}
/**
* Set the ID of the selected provider
* @param provider_id A provider identifier.
*/
function setSelectedProvider(provider_id) {
    getAccessEnabler().setSelectedProvider(provider_id);
}
/**
* Find the currently selected provider.
* @return An object with two properties:
* - MVPD: contains the currently selected provider ID, or null if no MVPD was selected;
* - AE_State: contains the Access Enabler's current authentication status for the user, one
*             of "New User", "User Not Authenticated" or "User Authenticated")
*/
function getSelectedProvider() {
    return getAccessEnabler().getSelectedProvider();
}
/**
* Requests a given metadata property value from the Access Enabler.
* There are two types of metadata: Static (Authentication token TTL,
* Authorization token TTL and Device ID) and User Metadata. The User
* Metadata type includes user specific information that is passed
* from the MVPD to the user’s device during the Authentication and/or
* Authorization flows.
*
* key is a String with the following meaning:
* If key is ‘TTL_AUTHN’ then the query is made to obtain the authentication
*   token expiration time.
* If key is ‘TTL_AUTHZ’ and args is an Array containing a resource id, then
*   the query is made to obtain the expiration time of the authorization
*   token associated to the specified resource.
* If key is ‘DEVICEID’ then the query is made to obtain the current device id.
*   Note that this feature is disabled by default and Programmers should contact
*   Adobe for information regarding enablement and fees.
* If key is a String different than the 3 values mentioned above then the query
*   is made for user metadata (the name of the metadata if given by key). The
*   list of available metadata keys is documented by Adobe.
*
* @param metadata key for which the value will be returned.
* @param args additional arguments
*/
function getMetadata(key) {
var args = arguments.splice(1);
getAccessEnabler().getMetadata(key, args);
}
/**
* Callbacks
*/
/**
* Called when the Access Enabler is successfully swfLoaded() and initialized.
* This is the entry point for your communication with the AE.
*/
var mvpdList;  // The list of cached MVPDs
var mvpdWindow;  // The reference to the popup with the MVPD login page
var cancelTimer = 0;
/* Callback indicating that the AccessEnabler swf has initialized */
function swfLoaded() {
   accessEnablerObject = $('#AccessEnabler').get(0);
   // Using a custom implementation of the MVPD picker
   accessEnablerObject.setProviderDialogURL('none');
   accessEnablerObject.setRequestor(requestorID);
}

function setConfig(configXML) {
   mvpdList = [];
   $.each($($.parseXML(configXML)).find('mvpd'), function (idx, item) {
      var mvpdId = $(item).find('id').text();
      mvpdList[mvpdId] = {
         displayName: $(item).find('displayName').text(),
         logo: $(item).find('logoUrl').text(),
         popup: $(item).find('iFrameRequired').text() == "true",
         width: $(item).find('iFrameWidth').text(),
         height: $(item).find('iFrameHeight').text()
      };

      $('#mvpdList').append($('<option value="' + mvpdId + '" title="' + mvpdId + '">'
       + mvpdList[mvpdId].displayName + '</option>'));
   });
   accessEnablerObject.getAuthentication();
}

function entitlementLoaded()
{
// TODO: Implement site-specific logic.
setRequestor("TNT");
getAuthentication();
}
/**
* Callback that receives the list of available providers for the current
* requestor ID.
*/
function displayProviderDialog(providers) {
// TODO: Implement a dialog to display MVPDs.
setSelectedProvider("Verizon");
}
/**
* Callback that receives the result of a successful authorization token request.
* Your implementation sets the authorization token.
*/
function setToken(requestedResourceID, token) {
    // TODO: Implement logic for sending the media token to be verified on your servers
    // by the entitlement verifier library.
}
/**
* Callback that indicates a failed authorization token request.
* @param requestedResourceID The resource ID for which the token request failed.
* @param requestErrorCode  The error code for the failure.
* @param requestErrorDetails The custom error message that describes the failure.
*/
function tokenRequestFailed(requestedResourceID, requestErrorCode, requestErrorDetails) {
    // TODO: Implement logic for handling a user who is not authorized to
    // access the resource. The error code and error details are specific to the distributor.
}
/** Callback that customizes the size of the provided selector dialog. **/
function setMovieDimensions(width, height) {
    //TODO: Set the dimension for the provider selector.
}
/**
* Callback that indicates the authentication status for the user.
*  @param isAuthenticated Authentication status is one of "1" (Authenticated) or
*                         "0" (Not authenticated).
*  @param errorCode Any error that occurred when determining the authentication status,
*                   or an empty string if no error occurred.
*/
function setAuthenticationStatus(isAuthenticated, errorCode) {
    // TODO: Start here implementing logic for handling if a user is authenticated or not.
}
/**
* Callback that creates an iFrame to use for login if the MVPD requires it.
*/
function createIFrame(width, height) {
// Create a new div to hold the iframe.
var mvpddiv = document.createElement('div');
mvpddiv.id = 'mvpddiv';
mvpddiv.style.position = 'absolute';
mvpddiv.style.display = 'block';
mvpddiv.style.top = '50px';
mvpddiv.style.left = '50%';
mvpddiv.style.zIndex = '100';
mvpddiv.style.background = 'sandybrown';
mvpddiv.style.marginLeft = '-' + width / 2 + 'px';
// Create the iframe to the specified width and height for the MVPD login page.
var iframe = document.createElement('iframe');
iframe.id = iframe.name = 'mvpdframe';
iframe.style.width = width + 'px';
iframe.style.height = height + 'px';

// Add elements to DOM.
mvpddiv.appendChild(iframe);
document.getElementsByTagName('body')[0].appendChild(mvpddiv);

// Force the name into the DOM since it is still not refreshed, for IE.
window.frames['mvpdframe'].name = 'mvpdframe';
}
/**
* Callback that sends a tracking data event and associated data
*  @param trackingEventType The type of event that triggered this tracking event
*  @param trackingData An array of all the tracking data/variables associated with
*                      the tracking event
*
* There are three possible tracking events types:
*    authorizationDetection    - any time an authorization token request returns
*    authenticationDetection    - any time an authentication check occurs
*    mvpdSelection                - when the user selects an mvpd in the mvpd selection dialog
* trackingData values:
* For trackingEventType "authorizationDetection"
*     [0] Whether the token request was successful [true/false]
*       and if true:
*       [1] MVPD ID [string]
*       [2] User ID (md5 hashed) [string]
*       [3] Whether it was cached or not [true/false]
*
* For trackingEventType "authenticationDetection"
*     [0] Whether the token request was successful (false)
*       and if successful is true:
*       [1] MVPD ID
*       [2] User ID (md5 hashed)
*       [3] Whether it was cached or not (true/false)
*
* For trackingEventType "mvpdSelection"
*       [0] MVPD ID
*
* MVPD Example: MVPD ID for Comcast is "Comcast"
*/
function sendTrackingData(trackingEventType, trackingData) {
// TODO: This is the entry point for sending tracking data in external tracking solutions.
}
/**
* Called when a get-metadata request has completed successfully.
*
* The args parameter is available only when the key is ‘TTL_AUTHZ’ or a user metadata key.
* For ‘TTL_AUTHZ’, args is an array containing the resource id. For a user metadata request,
* args is a Boolean value that specifies whether the returned metadata is encrypted or not.
* For all other requests, args is null and should be ignored.
*
* For simple requests (‘TTL_AUTHN’, ‘TTL_AUTHZ’, ‘DEVICEID’), result is a String (representing
* the Authentication TTL, Authorization TTL or Device ID). In the case of a User Metadata
* request, result can be a primitive or JSON object representing the metadata payload. The
* exact structure of the user metadata objects is documented by Adobe.
*
* @param key The name of the metadata for which the request was made.
* @param args Varies depending upon the request type. (See comments above.)
* @param result Varies depending upon the request type. (See comments above.)
*/
function setMetadataStatus(key, args, result) {
// TODO: Start here implementing logic for handling the metadata that has been returned.
}