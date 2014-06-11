/* FlashPlayer version required */
var flashPlayerVersion = "10.1.53";

/* AccessEnabler location */
var AccessEnablerLocation = "http://entitlement.auth.adobe.com/entitlement/AccessEnabler.swf";

/* AccessEnabler ID */
var AccessEnablerID = "AccessEnabler";

function loadAccessEnabler() {
    var flashvars = {};
    var params = {};
    params.menu = "false";
    params.quality = "high";
    params.bgcolor = "#33FF22";
    params.AllowScriptAccess = "always";
    params.swliveconnect = "true";
    params.wmode = "transparent";
    params.align = "middle";
    var attributes = {};
    attributes.id = AccessEnablerID;
    attributes.name = AccessEnablerID;
    swfobject.embedSWF(AccessEnablerLocation, "contentAccessEnabler",
                       "492", "323", flashPlayerVersion, "swf/expressInstall.swf",
                       flashvars, params, attributes)
}

/** Obtain the local shared object for the Access Enabler. */
function getAccessEnabler() {
    return document.getElementById(AccessEnablerID);
​}