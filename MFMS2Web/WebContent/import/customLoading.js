/**
 * 
 */

function showCustomLoading() {
	var body = document.getElementsByTagName("BODY")[0];

	var divLoadScreen = document.createElement("div");
	divLoadScreen.id = "custom_load_screen";
	divLoadScreen.style.background = "#e6e6e6";
	divLoadScreen.style.opacity = "0.8";
	divLoadScreen.style.position = "fixed";
	divLoadScreen.style.zIndex = "9998";
	divLoadScreen.style.top = "0px";
	divLoadScreen.style.width = "100%";
	divLoadScreen.style.height = "100%";

	var divLoading = document.createElement("div");
	divLoading.id = "custom_loading";
	divLoading.className = "pageLoadingAnimation";

	// divLoading.style.position= "fixed";
	// divLoading.style.left="0px";
	// divLoading.style.top= "0px";
	// divLoading.style.width= "100%";
	// divLoading.style.height= "100%";
	// divLoading.style.zIndex= "9999";
	// divLoading.style.background= 'url("import/img2/loader-32x/ring-alt.gif")
	// center no-repeat #fff';

	divLoadScreen.appendChild(divLoading);
	body.appendChild(divLoadScreen);

}

function hideCustomLoading() {

	var divLoadScreen = document.getElementById("custom_load_screen");
	var divLoading = document.getElementById("custom_loading");

	if (divLoadScreen != null) {
		divLoading.parentNode.removeChild(divLoading);
		divLoadScreen.parentNode.removeChild(divLoadScreen);
	}
	// divLoadScreen.remove();
	// divLoading.remove();
}