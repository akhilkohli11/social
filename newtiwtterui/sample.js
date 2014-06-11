$(function() {
    embedAE();
});
function embedAE() {
    var flashvars = {};
    var params = {
        bgcolor: "#869ca7",
        quality: "high",
        allowscriptaccess: "always"
    };
    var attributes = {
        id: "ae_swf",
        align: "middle",
        name: "ae_swf"
    };
    swfobject.embedSWF(
            "https://entitlement.auth-staging.adobe.com/entitlement/AccessEnabler.swf",
            "alternative", "1", "1", "10.1.53", "", flashvars, params, attributes);
}
function getAE() {
    return document.getElementById("ae_swf");
}
function swfLoaded() {
    var swf = getAE();
    initBindings();
    // don't load the default provider-selection dialog
    swf.setProviderDialogURL("none");
    // identify yourself
    swf.setRequestor("TNT");
    swf.checkAuthentication();
}
// Define the button actions for the provider dialog
function initBindings() {
    var provider_selector = $('#provider_selector');
    var sign_in = $('#sign_in');
    var sign_out = $('#sign_out');
    var login = $('#login');
    var cancel = $('#cancel');
    var request_access = $('#request_access');
    sign_out.click(function() {
        getAE().logout();
    });
    sign_in.click(function() {
        getAE().getAuthentication();
    });
    login.click(function() {
        var selected_mvpd_id = $("#provider_list option:selected").val();
        getAE().setSelectedProvider(selected_mvpd_id);
    });
    cancel.click(function() {
        getAE().setSelectedProvider(null);
        provider_selector.hide();
    });
    request_access.click(function(){
        getAE().getAuthorization("TNT");
    });
}
// Called if user is already authenticated; queries AE to find the current user's MVPD,
// Adds the result to the UI
function setDistributor() {
    var distributor = $("#distributor");
    var selectedProvider = getAE().getSelectedProvider();
    distributor.replaceWith('<div id="distributor">' +
                            'Courtesy of ' +
                            selectedProvider.MVPD +
                            '</div>');
}
// Adjust the contents of the provider dialog, based on authentication status
// Adds the call to check and display the current distributor, if the user is already
// logged in.
function setAuthenticationStatus(isAuthenticated, errorCode) {
    var user_authenticated = $("#user_authenticated");
    var video_player = $("#video_player");
    var sign_in = $('#sign_in');
    var sign_out = $('#sign_out');
    var request_access = $('#request_access');
    if (isAuthenticated == 1) {
        setDistributor();
        user_authenticated.replaceWith('<div id="user_authenticated">
                                       You are authenticated - if you wish you can sign out
                                       </div>');
        sign_out.show();
        sign_in.hide();
        video_player.replaceWith('<div id="video_player">Now you can ask for access.</div>');
        request_access.show();
    } else {
        user_authenticated.replaceWith('<div id="user_authenticated">
                                       You are not authenticated please sign in
                                       </div>');
        sign_in.show();
        sign_out.hide();
    }
}
// Allow user to choose a provider
function displayProviderDialog(providers) {
    var provider_list = $("#provider_list");
    var options = provider_list.attr('options');
    for (var index in providers) {
        options[index] = new Option(providers[index].displayName,
                                    providers[index].ID);
    }
    //select the first one by default
    if (!$("#provider_list option:selected").length)
        $("#provider_list option[0]").attr('selected', 'selected');
    $('#provider_selector').show();
}

function createIFrame(width, height) {
    // Move the div to be centered on the page relative to the size of the iframe.
    var mvpddiv = document.getElementById('mvpddiv');
    mvpddiv.style.position = "absolute";
    mvpddiv.style.display = "block";
    mvpddiv.style.top = "50px";
    mvpddiv.style.left = "50%";
    mvpddiv.style.zIndex = "100";
    mvpddiv.style.background = "sandybrown";
    mvpddiv.style.marginLeft = "-" + width / 2 + "px";

    // Create the iframe to the specified width and height for the MVPD login page.
    var iframe = document.getElementById("mvpdframe");
    iframe.style.width = width + "px";
    iframe.style.height = height + "px";

    // Force the name into the DOM since it is still not refreshed, for IE.
    window.frames["mvpdframe"].name = "mvpdframe";
}

// Handle the authorization response by sending token to video player
function setToken(requested_resource_id, token) {
    var video_player = $("#video_player");
    var request_access = $("#request_access");
    video_player.replaceWith('<div id="video_player">Now you are authorized to ' +
                             requested_resource_id +
                             ' content with ' + token + ' . </div>');
}