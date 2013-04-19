/*
 * Copyright (c) 2010-2012 - The Amdatu Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var org = org || {};
org.amdatu = org.amdatu || {};

org.amdatu.osContainer = function(containerId) {
	var config = {};
	config[osapi.container.ContainerConfig.RENDER_DEBUG] = '0';

	this.container = new osapi.container.Container(config);

	config[osapi.container.ContainerConfig.GET_PREFERENCES] = this.loadPreferences();
	config[osapi.container.ContainerConfig.SET_PREFERENCES] = this.savePreferences();

	// XXX Gadget preferences are not persisted; this is something the user
	// (read: developer) should do...
	this.container.gadgetPrefsCache = {};
	this.container.gadgetId = 0;
	this.container.gadgetContainerId = containerId;
	this.container.gadgetTemplate = '<div id="gadget-X" class="gadget"><div class="gadget-header form-inline">header</div><div id="gadget-site" class="gadget-content"></div></div>';

	this.container.managedHub = new OpenAjax.hub.ManagedHub({
		onSubscribe : function(topic, container) {
			console.log(container.getClientID() + " subscribes to this topic '" + topic + "'");
			return true; // return false to reject the request.
		},
		onUnsubscribe : function(topic, container) {
			console.log(container.getClientID() + " unsubscribes from this topic '" + topic + "'");
			return true;
		},
		onPublish : function(topic, data, pcont, scont) {
			console.log(pcont.getClientID() + " publishes '" + data + "' to topic '" + topic + "' subscribed by " + scont.getClientID());
			return true; // return false to reject the request.
		}
	});
	gadgets.pubsub2router.init({
		hub : this.container.managedHub
	});

	try {
		this.container.inlineClient = new OpenAjax.hub.InlineContainer(this.container.managedHub, 'AmdatuOpenSocialContainer', {
			Container : {
				onSecurityAlert : function(source, alertType) {
					console.log("Security alert: " + alertType);
				},
				onConnect : function(container) {
					console.log("Connected: " + container.getClientID());
				},
				onDisconnect : function(container) {
					console.log("Disconnected: " + container.getClientID());
				}
			}
		});
		this.container.inlineClient.connect();
	} catch (e) {
		console.log('Error creating or connecting to inline client: ' + e.message);
	}
};

/**
 * Returns a function that loads the gadget preferences for the gadget denoted
 * by the given URL, accepting the following parameters:
 * 
 * @param siteId
 *            the ID of the DOM-element where the gadget is rendered;
 * @param gadgetURL
 *            the URL of the gadget to retrieve the preferences for;
 * @param callback
 *            the callback for returning the loaded preferences. This callback
 *            <b>must</b> be called in order to render the gadget properly!
 * @returns a function accepting the above mentioned parameters.
 */
org.amdatu.osContainer.prototype.loadPreferences = function() {
	var that = this;
	return function(siteId, gadgetURL, callback) {
		console.log('Loading preferences for ' + gadgetURL);

		var preferences = that.container.gadgetPrefsCache[gadgetURL] || {};
		callback(preferences);
	};
}

/**
 * Returns a function that saves the preferences of the gadget denoted by the
 * given URL, accepting the following parameters:
 * 
 * @param siteId
 *            the ID of the DOM-element where the gadget is rendered;
 * @param gadgetURL
 *            the URL of the gadget to save the preferences for;
 * @param preferences
 *            the actual preferences to save.
 * @returns a function accepting the above mentioned parameters.
 */
org.amdatu.osContainer.prototype.savePreferences = function() {
	var that = this;
	return function(siteId, gadgetURL, preferences) {
		console.log('Saving preferences for ' + gadgetURL + ' (' + preferences + ')');

		if (that.container.gadgetPrefsCache[gadgetURL]) {
			$.extend(that.container.gadgetPrefsCache[gadgetURL], preferences);
		} else {
			that.container.gadgetPrefsCache[gadgetURL] = preferences;
		}
	};
}

/**
 * Closes the gadget.
 * 
 * @param gadgetSite
 *            the gadget site of the gadget to close.
 */
org.amdatu.osContainer.prototype.closeGadget = function(gadgetSite) {
	this.container.closeGadget(gadgetSite);
}

/**
 * Toggles the view state of the given gadget.
 * 
 * @param gadget
 *            the DOM-element representing the gadget to toggle the view state
 *            for.
 * @return the new state of the gadget, either 'home' or 'canvas'.
 */
org.amdatu.osContainer.prototype.toggleState = function(gadget) {
	var state = $(gadget).data('state');
	if (state === 'canvas') {
		state = 'home';
	} else {
		state = 'canvas';
	}
	$(gadget).data('state', state);
	return state;
}

/**
 * Returns the parameters for rendering a gadget on screen.
 * 
 * @param gadget
 *            the gadget for which the rendering parameters should be returned;
 * @param state
 *            the view-state to render the gadget in.
 * @return the rendering parameters as object.
 */
org.amdatu.osContainer.prototype.getRenderParameters = function(gadget, state) {
	var renderParams = {};
	renderParams[osapi.container.RenderParam.VIEW] = state || 'default';

	if (state === 'canvas') {
		// "full screen"
		renderParams[osapi.container.RenderParam.HEIGHT] = '100%';
		renderParams[osapi.container.RenderParam.WIDTH] = '100%';
	} else {
		renderParams[osapi.container.RenderParam.HEIGHT] = '200';
		renderParams[osapi.container.RenderParam.WIDTH] = '320';
	}

	return renderParams;
}

/**
 * Handles the gadget navigation action.
 * 
 * @param gadget
 *            the gadget DOM-element for which to handle the navigation action;
 * @param gadgetSite
 *            the gadget site for which to handle the navigation action;
 * @param gadgetURL
 *            the URL of the gadget;
 * @param actionName
 *            the name of the action to handle.
 */
org.amdatu.osContainer.prototype.handleNavigateAction = function(gadget, gadgetSite, gadgetURL, actionName) {
	if (gadgetSite) {
		if (actionName === 'remove') {
			this.removeGadget(gadget, gadgetSite, gadgetURL);
		} else {
			var viewParams = {};
			var renderParams = this.getRenderParameters(gadget, this.toggleState(gadget));

			this.container.navigateGadget(gadgetSite, gadgetURL, viewParams, renderParams, 
					function(gadgetMetadata) {
						if (gadgetMetadata.error) {
							console.log('There was an error navigating to the gadget at ' + gadgetURL + '\n: ' + gadgetMetadata.error.message);
						}
					});
		}
	}
}

/**
 * Creates and adds a new HTML-element to the gadget container in which a gadget
 * can be rendered.
 * 
 * @param metadata
 *            the gadget's metadata;
 * @param gadgetURL
 *            the URL of the gadget itself.
 * @return the DOM element representing the gadget site.
 */
org.amdatu.osContainer.prototype.prepareGadgetElement = function(metadata, gadgetURL) {
	var curId = ++this.container.gadgetId;
	var gadgetSiteId = 'gadget-site-' + curId;
	var gadgetId = 'gadget-' + curId;
	var gadgetTitle = (metadata['modulePrefs'] && metadata['modulePrefs'].title) || 'Title not set';

	var template = this.container.gadgetTemplate.replace(/gadget-X/g, gadgetId).replace(/gadget-site/g, gadgetSiteId);

	$(template)
		.appendTo($(this.container.gadgetContainerId))
		.find('.gadget-header')
		.text('')
		.append('<a href="#" id="gadget-title-' + curId + '" class="btn btn-mini">' + gadgetTitle + '</a>')
		.append('<a href="#" id="remove" class="btn btn-mini"><i class="icon-remove"></i></a>');

	var that = this;
	// determine which button was clicked and handle the appropriate event.
	$('#' + gadgetId + ' .gadget-header .btn').click(function() {
		var gadget = $(this).closest('.gadget');
		var gadgetSite = gadget.find('.gadget-content').data('gadgetSite');
		that.handleNavigateAction(gadget, gadgetSite, gadgetURL, this.id);
		return false;
	});

	console.log('Prepared gadget element for: ' + gadgetURL + '; ' + gadgetSiteId);

	return $('#' + gadgetSiteId).get([ 0 ]);
}

/**
 * Renders the gadget with the given URL into the given element.
 * 
 * @param gadgetURL
 *            the URL of the gadget to render;
 * @param gadgetElement
 *            the DOM-element in which the gadget should be rendered.
 * @return the gadget site object.
 */
org.amdatu.osContainer.prototype.renderGadget = function(gadgetURL, gadgetElement) {
	console.log('Rendering gadget: ' + gadgetURL);

	var site = this.container.newGadgetSite(gadgetElement);

	var viewParams = {};
	var renderParams = this.getRenderParameters(gadgetElement);

	this.container.navigateGadget(site, gadgetURL, viewParams, renderParams,
			function(gadgetMetadata) {
				if (gadgetMetadata.error) {
					console.log('There was an error rendering the gadget at ' + gadgetURL + '\n: ' + gadgetMetadata.error.message);
				}
			});

	return site;
};

/**
 * Creates a single gadget to the gadget container.
 * 
 * @param metadata
 *            the gadget's metadata;
 * @param gadgetURL
 *            the URL of the gadget itself.
 * @return the gadget site object.
 */
org.amdatu.osContainer.prototype.createGadget = function(metadata, gadgetURL) {
	console.log('Creating gadget: ' + gadgetURL);

	var element = this.prepareGadgetElement(metadata, gadgetURL);
	var site = this.renderGadget(gadgetURL, element);
	// Associate the gadget site with its DOM-element, allowing it to be retrieved easily...
	$(element).data('gadgetSite', site);

	return site;
}

/**
 * Preloads and renders a gadget with the given URL.
 * 
 * @param gadgetURL
 *            the URL of the gadget to load.
 */
org.amdatu.osContainer.prototype.loadGadget = function(gadgetURL) {
	var that = this;
	this.container.preloadGadget(gadgetURL, function(metadata) {
		var gadgetMetadata = metadata[gadgetURL] || {};
		if (gadgetMetadata.error) {
			console.log('There was an error loading the gadget at ' + gadgetURL + '\n: ' + gadgetMetadata.error.message);
		} else {
			that.createGadget(gadgetMetadata, gadgetURL);
		}
	});
}

/**
 * Allows the metadata for the gadget specified by the given URL to be
 * retrieved.
 * 
 * @param gadgetURL
 *            the URL of the gadget to retrieve the metadata for;
 * @param callback
 *            the callback function to call when the metadata is retrieved.
 */
org.amdatu.osContainer.prototype.getGadgetMetadata = function(gadgetURL, callback) {
	this.container.getGadgetMetadata(gadgetURL, callback);
}

/**
 * Adds a gadget with the given URL and identifier to the preferences of the
 * calling user.
 * 
 * @param gadgetURL
 *            the URL of the gadget specification to add.
 */
org.amdatu.osContainer.prototype.addGadget = function(gadgetURL) {
	console.log('Adding gadget: ' + gadgetURL);

	this.loadGadget(gadgetURL);
}

/**
 * Removes the gadget with the given identifier from the preferences of the
 * calling user.
 * 
 * @param gadget
 *            the DOM-element representing the actual gadget;
 * @param gadgetSite
 *            the gadget site object to remove;
 * @param gadgetURL
 *            the URL of the gadget to remove.
 */
org.amdatu.osContainer.prototype.removeGadget = function(gadget, gadgetSite, gadgetURL) {
	console.log('Removing gadget: ' + gadgetURL);

	// Close gadget itself...
	this.closeGadget(gadgetSite);
	// Remove DOM element...
	gadget.remove();
}

/**
 * MAIN ENTRY POINT
 */
$(document).ready(function() {
	var container = new org.amdatu.osContainer('#container');

	// Fetch all available gadgets and show them in a simple
	// drop down list...
	$.ajax({
		url : '/gadgetCollections',
		dataType : 'json',
		success : function(data) {
			var gadgetCollection = $('#gadgetCollection');
			var gadgetUrls = [];
			$.each(data.collections, function(i, gadgetData) {
				$.each(gadgetData.apps, function(i, url) {
					container.getGadgetMetadata(url, function(metadata) {
						if (metadata[url].error) {
							console.log("Failed to retrieve gadget metadata for: " + url + '\n: ' + metadata[url].error.message);
						} else {
							var name = metadata[url].modulePrefs.title;
							gadgetUrls.push(url);
							gadgetCollection.append('<option value="' + url	+ '">' + name + '</option>');
						}
					});
				});
			});
		},
		error : function() {
			console.log("Failed to obtain a list of all gadgets!");
		}
	});

	// Add a single gadget to the site area
	$('#addGadget').click(function() {
		var gadgetUrls = ($('#gadgetCollection').val() || '').split(',');

		$.each(gadgetUrls, function(i, url) {
			container.addGadget(url);
		});
		return true;
	});
});
