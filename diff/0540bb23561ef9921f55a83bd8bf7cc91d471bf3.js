var PocketQuery = PocketQuery || {};
($ || jQuery).extend(PocketQuery, (function($) {

	// pattern that must be in DB URL => driver classpath
	var DRIVER_MAPPINGS = {
		mysql: 'com.mysql.jdbc.Driver', // MySQL
		sqlserver: 'net.sourceforge.jtds.jdbc.Driver', // MSSQL
		oracle: 'oracle.jdbc.driver.OracleDriver', // Oracle
		db2: 'COM.ibm.db2.jdbc.net.DB2Driver', // DB2
		sybase: 'com.sybase.jdbc.SybDriver', // Sybase
		postgresql: 'org.postgresql.Driver', // PostgreSQL
		hsqldb: 'org.hsqldb.jdbc.JDBCDriver' // HSQLDB
	};

	var CODEMIRROR_SETTINGS = {
		indentWithTabs: true,
		smartIndent: true,
		lineNumbers: true,
		matchBrackets : true,
		tabSize: 2,
		autoClearEmptyLines: false,
		lineWrapping: true
	};

	var PARAMETER_TYPES = ['String', 'Integer', 'ListOfStrings', 'ListOfIntegers', 'Boolean', 'Constant'];

	// How many items can at least be seen simultaneously in an entity list?
	// We hold this hard-coded for performance reasons.
	var NUM_VISIBLE_ITEMS_IN_LIST = 17;

	var soyAlias;

	var $body, $wrapper, $container, $menu;
	var entityStore = {}, settingsStore = {};

	// Somehow eslint reports this variable not being used, but it's used later on. Ignoring line.
	var $itemLastClicked = null; // eslint-disable-line no-unused-vars

	// Defined here because it is used in $.fn function

	function $getActiveBox() {
		return $('.nice-box.active');
	}

	function toggleEmptyInfo(skipEmptyCheck) {
		var $box = $getActiveBox();
		var $ul = $box.find('.nice-list');
		var $niceInfo = $box.find('.nice-info');
		if (skipEmptyCheck || $ul.find('li').length > 0) {
			$niceInfo.addClass('hidden');
		} else {
			$niceInfo.removeClass('hidden');
		}
	}

	$.fn.niceDisable = function() {
		var $this = $(this);
		$this.find('.nice-extend-textarea').addClass('hidden');
		$this.find('input, textarea, select').attr('disabled', 'disabled');
	};

	$.fn.niceEnable = function() {
		var $this = $(this);
		var $nameInput = $this.find('input[id$="-name"]');
		$this.niceEntityName() ? $nameInput.attr('readonly', 'readonly') : $nameInput.removeAttr('readonly');
		$this.find('.nice-extend-textarea').removeClass('hidden');
		$this.find('input, textarea, select').removeAttr('disabled');
	};

	$.fn.niceClean = function() {
		var $form = $(this).closest('form');
		$form.find('.nice-heading').html('');
		$form.find('input:not([name="entityType"], [type="submit"]), textarea').val('');
		$form.find('input[type="checkbox"]').prop('checked', false);
		$form.find('option').removeAttr('selected');
		$form.find('select').find('option:first').attr('selected', 'selected');
		$form.niceDestroyEditor();
		$form.find('.key-value-container').remove();
		$form.find('.key-value-pairs').removeClass('hidden');
		$form.find('.expand-content').css('display','none');
		$form.find('.expand-content li').css('display','none');
		$form.removeClass('changes');
		$form.niceStatus('');
		$form.niceEnable();
	};

	$.fn.niceEntityType = function() {
		return $(this).closest('[data-entitytype]').attr('data-entitytype') || null;
	};

	$.fn.niceEntityName = function() {
		var $this = $(this);
		return $this.closest('[data-entityname]').attr('data-entityname')
			|| $this.find('input[id*="-name"]').val()
			|| null;
	};

	$.fn.niceRemoveFromList = function(entityName) {
		var $this = $(this);
		var $list = $this.closest('ul');
		if (entityName) {
			$list.find('[data-entityname="' + entityName + '"]').remove();
		} else {
			$this.closest('li').remove();
		}
		toggleEmptyInfo();
	};

	$.fn.niceStatus = function(message, error) {
		var $status = $(this).closest('.nice-left, .nice-right').find('.nice-status');
		$status.html(message)[error ? 'addClass' : 'removeClass']('error');
	};

	$.fn.niceSaveCode = function() {
		$(this).find('textarea.codemirror-active').each(function() {
			$(this).data('codemirror').save();
		});
	};

	$.fn.niceDestroyEditor = function() {
		$(this).find('textarea.codemirror-active').each(function() {
			var codemirror = $(this).data('codemirror');
			codemirror.setValue('');
			codemirror.toTextArea();
		});
	};

	$.fn.niceKeyValue = function(opts) {
		$(this).each(function() {
			var $input = $(this);
			var keyValueObj = PocketQuery.stringToObject($input.val(), {decodeParameterValues: true});
			var options = opts || {};
			var $div = $(soyAlias.keyvaluelist({
				keyValueObj: keyValueObj,
				options: options
			}));
			var update = function() {
				var object = {};
				$div.find('li').each(function() {
					var $li = $(this);
					var key = $li.find('.key').val();
					var value = $li.find('.value').val();
					if (key && value) {
						object[key] = value;
					}
				});
				$input.val(PocketQuery.objectToString(object, {encodeParameterValues: true}));
			};
			$div.on('click', 'span.add', function() {
				var $keyValueItem = $(soyAlias.keyvalueitem({options: options}));
				$div.find('ul').append($keyValueItem);
			});
			$div.on('click', 'span.remove', function(e) {
				$(e.target).closest('li').remove();
				update();
			});
			$div.on('keyup', 'input', update);
			$div.on('change', 'select', update);

			$input.after($div).addClass('hidden');
		});
	};

	function getText(key, param) {
		return AJS.I18n.getText('pocketquery.' + key, param);
	}

	function activateExpandTriggers() {
		if($('.expand-trigger')) {
			$('.expand-trigger').off('click').on('click',function(){
				$('.expand-content').slideToggle();
			});
		}
	}

	function storeSettings(key, value) {
		settingsStore[key] = value;
	}

	function getSettings(key) {
		return settingsStore[key];
	}

	function storeEntity(type, name, entity) {
		if (!entityStore[type]) {
			entityStore[type] = {};
		}
		entityStore[type][name] = entity;
	}

	function removeFromStore(type, key) {
		if (entityStore[type][key]) {
			delete entityStore[type][key];
		}
	}

	function getEntity(type, key) {
		return entityStore[type] && entityStore[type][key] || null;
	}

	function getEntitiesAsArray(type) {
		var entitiesArr = [];
		var entitiesObj = entityStore[type];
		if (entitiesObj) {
			$.each(entitiesObj, function(entityName, entity) {
				entitiesArr.push(entity);
			});
		}
		return entitiesArr;
	}

	function entityToForm(entityType, name) {
		var entity = getEntity(entityType, name);
		var $form = $('#nice-form-' + entityType);
		$.each(entity, function(key, value) {
			var $elem = $form.find('#' + entityType + '-' + key);
			if ($elem.is('select')) {
				$elem.find('option[value="' + entity[key] + '"]').attr('selected', 'selected');
			} else if ($elem.is('[type="checkbox"]') && !!value) {
				$elem.prop('checked', true);
			} else {
				$elem.val(value);
			}
		});
		// fill usage .usage-display und .content-expander if entityType != query
	}

	function storeEntityFromForm(form) {
		var $form = $(form);
		var entityType = $form.niceEntityType();
		var entityName = $form.niceEntityName();
		var entity = {};
		var selector = 'input[type="text"],input[type="password"],input[type="checkbox"],textarea,select';
		$form.find('.field-group').children(selector).each(function() {
			var $this = $(this);
			var prop = $this.closest('.field-group').attr('data-field');
			var value;
			if ($this.is('select')) {
				value = $.trim($this.find('option:selected').val());
			} else if ($this.is('[type="checkbox"]')) {
				value = $this.is(':checked');
			} else {
				value = $.trim($this.val());
			}

			entity[prop] = value;
		});
		storeEntity(entityType, entityName, entity);
		return entity;
	}

	function confirm(text, okFn) {
		var dialog = new AJS.Dialog({
			width: 400,
			height: 140,
			id: 'nice-confirm-dialog'
		});
		dialog.addSubmit(getText('ok'), function() {
			dialog.remove();
			okFn && okFn();
		});
		okFn && dialog.addCancel(getText('cancel'), function() {
			dialog.remove();
		});

		dialog.addPanel('', '');
		dialog.getCurrentPanel().html($('<p>').html(text));
		dialog.show();
	}

	function pocketPost(url, data, callback) {
		$.post(url, data, function(data) {
			if (typeof data === 'string') {
				confirm(getText('admin.confirm.logged.out'));
			} else {
				callback && callback(data);
			}
		});
	}

	function showActiveForm(shallShow) {
		var $activeBox = $getActiveBox();
		var $niceRight = $activeBox.find('.nice-right');
		var isEdit = !!$niceRight.find('.nice-form').find('input[name="entityName"]').val();
		if (shallShow) {
			$activeBox.addClass('form-visible');
			$activeBox[isEdit ? 'removeClass' : 'addClass']('add');
			$niceRight.removeClass('hidden');
		} else {
			$activeBox.removeClass('form-visible edit add');
			$niceRight.addClass('hidden');
		}
	}

	function addItemSorted($ul, $li) {
		var entityName = $li.niceEntityName();
		var entityNameLowerCase = entityName.toLowerCase();
		$ul.prepend($li);
		while ($li.next().length && $li.next().niceEntityName().toLowerCase() <= entityNameLowerCase) {
			$li.next().after($li);
		}
	}

	// The first item in the list that is not hidden should get the class "first-visible" such that we can
	// style that item accordingly.
	function handleFirstVisibleListItem($list) {
		$list.find('li:not(.hidden):first').addClass('first-visible').siblings().removeClass('first-visible');
	}

	function scrollToItemIfAppropriate($li) {
		// the index of the item in the list is also the number of elements before the item
		var numItems = $li.siblings().length + 1;
		var $ul, itemHeight, itemTopPos, listHeight, scrollTop, index;
		// don't do anything if all items can be seen and we don't need scroll
		if (numItems > NUM_VISIBLE_ITEMS_IN_LIST) {
			index = $li.index();
			$ul = $li.parent();
			// measure the height of the 2. list item because the first one has an additional top border
			itemHeight = $li.siblings().eq(1).outerHeight();
			listHeight = $ul.outerHeight();
			scrollTop = $ul.scrollTop(); // current scroll position
			itemTopPos = index * itemHeight;
			if (scrollTop > itemTopPos) { // if the new item is above the scroll port
				// scroll to the elements position by calculating the height of all its previous items
				$ul.scrollTop(index * itemHeight);
			} else if (scrollTop + listHeight < itemTopPos) { // if the new item is below the scroll port
				// make the new item appear on the very bottom (here we have to include the border size of 1
				$ul.scrollTop((index * itemHeight) - (listHeight - itemHeight - 1));
			}
		}
	}

	function addItemToList(entityName) {
		var $box = $getActiveBox();
		var $list = $box.find('.nice-list');
		var options = { name: entityName, active: true };
		var $newItem = $(soyAlias.listitem(options));
		$list.removeClass('all-hidden'); // if an item is added the list won't have hidden items only
		$box.find('.nice-info').addClass('hidden');
		$list.find('li.active').removeClass('active');
		addItemSorted($list, $newItem);
		handleFirstVisibleListItem($list); // if an item was added this might now be the first visible item
		scrollToItemIfAppropriate($newItem);
	}

	function updateQueryInUsageListItems(queryName, templateName, converterName, databaseName) {
		var $usageListItems = $('.usage-display .expand-content li[data-queryName=' + queryName + ']');

		$usageListItems.each(function(){
			var $this = $(this);
			$this.attr('data-template', templateName);
			$this.attr('data-converter', converterName);
			$this.attr('data-database', databaseName);
		});
	}

	function addQueryToUsageListItems(queryName, templateName, converterName, databaseName) {
		var $usageLists = $('.usage-display.entities-in-queries .expand-content ul');
		var options = { query: queryName, template: templateName, converter: converterName, database: databaseName };

		$usageLists.each(function() {
			var $newItem = $(soyAlias.usagelistitem(options));
			$(this).prepend($newItem);
			while($newItem.next().length && $newItem.next().attr('data-queryname').toLowerCase() <= queryName) {
				$newItem.next().after($newItem);
			}
		});
	}

	function removeQueryFromUsageListItems(queryName) {
		var $itemsToRemove = $('.usage-display.entities-in-queries .expand-content li[data-queryName=' + queryName + ']');
		$itemsToRemove.each(function() {
			$(this).remove();
		});
	}

	function handleFieldsForEntity($form) {
		var entityType = $form.niceEntityType();
		var databaseType, databaseName, databaseEntity, fieldsToShow;
		switch (entityType) {
			case 'database':
				databaseType = parseInt($('select#database-type').val(), 10);
				break;
			case 'query':
				databaseName = $('select#query-database').val();
				databaseEntity = PocketQuery.getEntity('database', databaseName);
				if (databaseEntity) {
					databaseType = databaseEntity.type;
				}
				break;
			default:
				// noop for other types so far
		}
		if ($.isNumeric(databaseType)) {
			$form.find('.field-group').removeClass('hidden');
			fieldsToShow = PocketQuery.getSettings(entityType + '-fields')[databaseType];
			$form.find('.field-group').each(function() {
				var $fieldgroup = $(this);
				var data = $fieldgroup.data();
				var field = data.field;
				if (fieldsToShow.indexOf(field) < 0) {
					$fieldgroup.addClass('hidden');
				}
			});
		}
	}

	function updateQueryFields() {
		var $select = $('#query-database');
		var $textarea = $('#query-statement');
		var databaseName = $select.val();
		var databaseEntity, databaseType, typeKey, language, codemirror;
		if (databaseName) {
			databaseEntity = PocketQuery.getEntity('database', databaseName);
			if (databaseEntity) {
				databaseType = parseInt(databaseEntity.type, 10);
				typeKey = databaseType === 0 ? 'statement' : 'resturl';
				language = databaseType === 0 ? 'text/x-sql' : '';
				codemirror = $textarea.data('codemirror');
				$textarea.attr('data-language', language);
				$textarea.closest('.field-group').find('.label-text').text(getText(typeKey));
				if (codemirror) {
					codemirror.setOption('lineNumbers', databaseType === 0);
					codemirror.setOption('mode', language);
				}
			}
		}
	}

	function confirmLostChanges(fn) {
		if ($getActiveBox().find('.nice-form').hasClass('changes')) {
			confirm(getText('admin.confirm.lost.changes'), fn);
		} else {
			fn();
		}
	}

	function onFormSubmit(e) {
		var $form = $(e.target);
		var doit = function() {
			var action = $form.attr('action');
			var entityType = $form.niceEntityType();
			var entityName = $form.niceEntityName();
			var oldEntityName = $form.find('[name="entityName"]').val();
			$form.find('input[type="text"]').each(function() {
				var $this = $(this);
				$this.val($.trim($this.val()));
			});
			$form.niceSaveCode();
			pocketPost(action, $form.serialize(), function(data) {
				var $option, $select;
				if (data.actionErrors) {
					$form.niceStatus(data.actionErrors[0], true);
				} else {
					$form.niceStatus(data.response);
					if (oldEntityName) {
						$form.closest('.nice-box').find('li[data-entityname="' + oldEntityName + '"]').remove();
						removeFromStore(entityType, entityName);
					}
					storeEntityFromForm($form);
					addItemToList(entityName);

					// update usage query lists
					if (entityType === 'query') {
						if (oldEntityName) {
							updateQueryInUsageListItems(entityName,
								$form.find('select#query-template').val(),
								$form.find('select#query-converter').val(),
								$form.find('select#query-database').val());
						} else {
							addQueryToUsageListItems(entityName,
								$form.find('select#query-template').val(),
								$form.find('select#query-converter').val(),
								$form.find('select#query-database').val());
						}
					}
					$form.niceClean();
					showActiveForm(false);

					// update options of select fields when editing/adding queries
					// $select = $('#query-' + entityType);
					if (entityType === 'database') {
						$select = $('#query-database');
					} else if (entityType === 'template') {
						$select = $('#query-template');
					} else if (entityType === 'converter') {
						$select = $('#query-converter');
					}
					if ($select) {
						if (oldEntityName) {
							$select.find('option[value="' + oldEntityName + '"]').val(entityName).html(entityName);
						} else {
							$option = $('<option>').val(entityName).html(entityName);
							$select.append($option);
						}
					}
				}
			});
		};
		e.preventDefault();
		doit();
	}

	function deactivateActivePanel() {
		var $box = $getActiveBox();
		var $form = $box.find('.nice-right form');
		var $addQueryLink = $('#pocket-queries').find('.nice-add-entity');
		var databases = getEntitiesAsArray('database');
		$box.find('.nice-list li').removeClass('active');
		toggleEmptyInfo();
		$form.niceClean();
		showActiveForm(false);
		$box.find('.nice-left').niceStatus('');
		if (!databases.length) {
			$addQueryLink.addClass('hidden');
		} else {
			$addQueryLink.removeClass('hidden');
		}
	}

	function onCancelClick(e) {
		e.preventDefault();
		confirmLostChanges(function() {
			deactivateActivePanel();
			updateFilteredEntityList($getActiveBox().find('select.nice-filter-dropdown'));
		});
	}

	function onRemoveItemClick(e) {
		e.preventDefault();
		confirm(getText('admin.confirm.remove'), function() {
			var $target = $(e.target);
			var $item = $target.closest('li');
			var $list = $item.closest('ul');
			var $form = $item.closest('.nice-box').find('form');
			var entityType = $target.niceEntityType();
			var entityName = $target.niceEntityName();
			var params = { entityType: entityType, entityName: entityName };
			$form.niceClean();
			showActiveForm(false);
			removeFromStore(entityType, entityName);
			$('select#query-'+entityType).find('option[value="' + entityName + '"]').remove();
			pocketPost('pocket-remove.action', params, function(data) {
				$item.niceStatus(data.response, !data.success);
				$target.niceRemoveFromList();
				if (entityType === 'query') {
					removeQueryFromUsageListItems(entityName);
				}
				handleFirstVisibleListItem($list); // if an item was removed, the first visible item might change
			});
		});
	}

	function initCodeMirror($form) {
		$form.find('textarea.code').each(function() {
			var $textarea = $(this);
			var language = $textarea.attr('data-language');
			var settings = $.extend(CODEMIRROR_SETTINGS, {
				mode: language
			});
			var codemirror = CodeMirror.fromTextArea(this, settings);
			$textarea.addClass('codemirror-active').data('codemirror', codemirror);
		});
	}

	function updateQueryUi($form) {
		var $inputCacheDuration = $('#query-cacheduration');
		var $labelLinkCacheDuration = $inputCacheDuration.closest('.field-group').find('.label-link');
		$form.find('#query-paramtypes').niceKeyValue({
			selectValues: PARAMETER_TYPES
		});
		updateQueryFields();
		if ($inputCacheDuration.val()) {
			$labelLinkCacheDuration.removeClass('hidden');
		} else {
			$labelLinkCacheDuration.addClass('hidden');
		}
	}

	// Based on a filter dropdown for the current entityType, change the value in the associated form
	function filterDropdownToForm(entityType) {
		var $activeBox = $getActiveBox();
		var $filterDropdown = $activeBox.find('select.nice-filter-dropdown');
		var filterValue = $filterDropdown.val();
		var filterType;
		if (filterValue) {
			filterType = $filterDropdown.attr('data-type');
			$activeBox.find('#'+entityType+'-'+filterType).val(filterValue);
		}

	}

	function handleAddEditClick($box, entityType, entityName) {
		var $form = $box.find('form');
		var $niceLeft = $box.find('.nice-left');
		if (entityName) { // edit
			$form.niceClean();
			$form.find('input[name="entityName"]').val(entityName);
			$form.find('.nice-heading').html(getText('admin.heading.'+entityType, entityName));
			entityToForm(entityType, entityName);
			filterQueryUsageList(entityType, entityName);
			updateQueryUsageCount(entityType, entityName);
		} else { // add
			toggleEmptyInfo(true);
			$niceLeft.find('li.active').removeClass('active');
			$form.niceClean();
			$box.find('.usage-display').css('display', 'none');
			// if this is an add operation, we might want to prefill an element in the form based on a filter dropdown
			filterDropdownToForm(entityType);
		}
		$form.niceEnable();
		handleFieldsForEntity($form);
		showActiveForm(true);
		initCodeMirror($form);
		$form.find('.key-value-pairs:not(.selects)').niceKeyValue();
		if (entityType === 'query') {
			updateQueryUi($form);
		}
		$box.find('.nice-left').niceStatus('');
	}

	function initDashboardTooltips() {
		$('#pocket-dashboard').find('.aui-iconfont-help').tooltip({
			title: function() {
				var entityType = $(this).niceEntityType();
				return getText('admin.entity.desc.'+entityType);
			},
			gravity: 'n',
			trigger: 'hover'
		});
	}

	function updateDashboardUi() {
		$('.nice-box[data-section="dashboard"]').html(soyAlias.dashboard({
			databases: getEntitiesAsArray('database'),
			queries: getEntitiesAsArray('query'),
			templates: getEntitiesAsArray('template'),
			converters: getEntitiesAsArray('converter'),
			numPocketQueryMacros: PocketQuery.getSettings('num-pocketquery-macros'),
			currentWarnings: PocketQuery.getSettings('current-warnings'),
			pqUsageStatisticsEnabled: PocketQuery.getSettings('pqUsageStatisticsEnabled'),
			macroUsageCountToday: PocketQuery.getSettings('macroUsageCountToday'),
			macroUsageCountMonth: PocketQuery.getSettings('macroUsageCountMonth'),
			queryCountCache: PocketQuery.getSettings('queryCountCache'),
			queryCountIndex: PocketQuery.getSettings('queryCountIndex'),
			queryCountSQL: PocketQuery.getSettings('queryCountSQL'),
			queryCountREST: PocketQuery.getSettings('queryCountREST'),
			statisticsTimestamp: PocketQuery.getSettings('statisticsTimestamp')
		}));
		initDashboardTooltips();
	}

	// Called whenever the filter dropdown in the active box should be updated.
	function updateActiveFilterDropdown() {
		var $activeBox = $getActiveBox(); // currently shown box container
		var $dropdownContainer = $activeBox.find('.nice-dropdown-container'); // currently active dropdown container
		var filterType, entities, $renderedDropdown, allLabel;
		if ($dropdownContainer.length) { // if an active dropdown container was found
			filterType = $dropdownContainer.attr('data-type'); // this is the entity type to filter by
			allLabel = getText('admin.filter.dropdown.all.'+filterType); // the text for the "All X" entry
			entities = getEntitiesAsArray(filterType); // all entities of the filter type
			$renderedDropdown = $(soyAlias.filterDropdown({allLabel: allLabel, type: filterType, collection: entities}));
			$dropdownContainer.html($renderedDropdown); // put the rendered dropdown into the dom
			$renderedDropdown.auiSelect2(); // activate the AUI select2 box
		}
	}

	function filterQueryUsageList(entityType, entityName) {
		var $activeBox = $getActiveBox(); // currently shown box container
		var $usageListItems = $activeBox.find('.usage-display .expand-content li');
		if (entityType === 'query') {
			entityType = entityType + '-' + entityName;
		}

		$usageListItems.each(function (index) {
			if ($(this).attr('data-' + entityType) === entityName) {
				$(this).css('display', 'list-item');
			}
		});
	}

	function updateQueryUsageCount(entityType, entityName) {
		var listedElementsSingular = entityType === 'query' ? 'page' : 'query';
		var listedElementsPlural = entityType === 'query' ? 'pages' : 'queries';
		var entityDataAttribute = entityType === 'query' ? entityType + '-' + entityName : entityType ;

		var $activeBox = $getActiveBox(); // currently shown box container
		var $usageDisplay = $activeBox.find('.usage-display');
		var $expandTrigger = $activeBox.find('.usage-display span.expand-trigger');

		var listItemsCount = $activeBox.find('.usage-display .expand-content li[data-' + entityDataAttribute + '=' + entityName + ']').length;
		var content ='no ' + listedElementsPlural;

		$usageDisplay.css('display', 'block');

		if (listItemsCount === 1) {
			content = '<a href="#">1 ' + listedElementsSingular + '</a>';
		} else if (listItemsCount > 1)  {
			content = '<a href="#">' + listItemsCount +' ' + listedElementsPlural + '</a>';
		}

		$expandTrigger.html(content);
	}

	function doChangeSection(section) {
		var clazz = 'aui-nav-selected';
		if (section === 'dashboard') {
			updateDashboardUi();
		}

		$menu.find('li[data-section="'+section+'"]').addClass(clazz).siblings().removeClass(clazz);
		$('.nice-box').removeClass('active');
		$('.nice-box[data-section="'+section+'"]').addClass('active');
		// anytime a section is changed, any filter dropdown in the new active box is updated
		updateActiveFilterDropdown();
	}

	function changeSection(section) {
		var stateObj;
		deactivateActivePanel();
		if (PocketQuery.shallUseHistoryInGeneral()) {
			stateObj = { section: section, origin: 'pocketquery', timestamp: new Date().getTime() };
			PocketQuery.logger.debug('History.pushState with data', stateObj);
			History.pushState(stateObj, document.title, '?section=' + section);
		} else {
			doChangeSection(section);
		}
	}

	function onEditItemClick(e) {
		var $target = $(e.target);
		var $box = $target.closest('.nice-box');
		var entityType = $target.niceEntityType();
		var entityName = $target.niceEntityName();
		e.preventDefault();
		confirmLostChanges(function() {
			handleAddEditClick($box, entityType, entityName);
		});
	}

	function onAddItemClick(e) {
		var $target = $(e.target).closest('a');
		var $box = $target.closest('.nice-box');
		var entityType = $box.attr('data-entitytype');
		e.preventDefault();
		confirmLostChanges(function() {
			handleAddEditClick($box, entityType);
		});
	}

	function onTestDatabaseClick(e) {
		var $button = $(e.target).closest('a');
		var $form = $button.closest('form');
		e.preventDefault();
		$form.niceStatus('');
		$button.addClass('loading');
		pocketPost('pocket-databasetest.action', $form.serialize(), function(data) {
			$button.removeClass('loading');
			if (data.actionErrors) {
				$form.niceStatus(data.actionErrors[0], true);
			} else {
				$form.niceStatus(data.response, !data.success);
			}
		});
	}

	function onClearCacheClick(e) {
		var $target = $(e.target);
		e.preventDefault();
		confirm(getText('admin.confirm.clearcache'), function() {
			var $form = $target.closest('form');
			var queryName = $form.niceEntityName();
			var params = { queryName: queryName };
			$form.niceStatus('');
			pocketPost('pocket-clearcache.action', params, function(data) {
				$form.niceStatus(data.response, !data.success);
			});
		});
	}

	function onItemClick(e) {
		var $target = $(e.target);
		var $li = $target.closest('li');
		var $box = $li.closest('.nice-box');
		var $siblings = $box.siblings();
		$siblings.find('.nice-right').addClass('hidden');
		$siblings.find('li').removeClass('active');
		$itemLastClicked = $li;
		$li.siblings('li').removeClass('active');
		$li.addClass('active');
		if (!$target.is('a')) {
			onEditItemClick(e);
		}
	}

	function onLeftAreaClick(e) { // clean status if not clicked on message itself (enable copy)
		var $target = $(e.target);
		if (!$target.closest('.error').length) {
			$(e.target).closest('.nice-left').niceStatus('');
		}
	}

	function onDatabaseDriverFocus() {
		var $driver = $('#database-driver');
		var dbUrl = $('#database-url').val();
		// Only do something if there's nothing in the driver input yet.
		if (!$driver.val()) {
			$.each(DRIVER_MAPPINGS, function(key, value) {
				if (dbUrl.indexOf(key) >= 0) {
					$driver.val(value);
				}
			});
		}
	}

	function onTypeSelectChange(e) {
		var $select = $(e.target);
		var $form = $select.closest('form');
		handleFieldsForEntity($form);
		if ($form.closest('.nice-box').attr('data-entitytype') === 'query') {
			updateQueryFields();
		}
		$form.niceStatus('');
	}

	function onFormContentChange() {
		$getActiveBox().find('.nice-form').addClass('changes');
	}

	function onMenuLinkClick(e) {
		var $a = $(e.target);
		var $li = $a.closest('li');
		var section = $li.attr('data-section');
		e.preventDefault();
		confirmLostChanges(function() {
			changeSection(section);
		});
	}

	function onContentLabelLinkClick(e) {
		var $fieldgroup = $(e.target).closest('.field-group');
		var entityType = $fieldgroup.niceEntityType();
		var $textarea = $fieldgroup.find('textarea');
		var codemirror = $textarea.data('codemirror');
		var settingKey = entityType === 'template' ? 'template.default' : 'converter.scaffold';
		var scaffoldCode = PocketQuery.getSettings(settingKey);
		var currentEditorContent = codemirror.getValue();
		var doAdd = function() {
			codemirror.setValue(scaffoldCode);
		};
		e.preventDefault();
		if (currentEditorContent) {
			confirm(getText('admin.confirm.lost.changes.editor'), doAdd);
		} else {
			doAdd();
		}
	}

	function onDashboardBoxClick(e) {
		var $target = $(e.target);
		var $box = $target.closest('.entity-box');
		var entityType = $box.attr('data-entitytype');
		e.preventDefault();
		if (!$target.hasClass('aui-iconfont-help')) {
			changeSection(entityType);
		}
	}

	// Disable window scroll on mouse enter in entity list
	function onListMouseEnter(e) {
		$body.addClass('noscroll');
	}

	// Enable window scroll on mouse enter in entity list
	function onListMouseLeave(e) {
		$body.removeClass('noscroll');
	}

	// Called when a change occurs in a filter dropdown. Shows/hides entities in the list accordingly.
	function onFilterDropdownChange(e) {
		var $filterDropdown = $(e.target); // the dropdown element (i.e. select box)

		// close active panel to avoid confusion (as active entity might not be included in the new filtered list)
		deactivateActivePanel();

		updateFilteredEntityList($filterDropdown);
	}

	// filterType: type by which we filter (e.g. "datasource" in case of query)
	function updateFilteredEntityList($filterDropdown){
		var $activeBox = $getActiveBox();
		var $list = $activeBox.find('.nice-list');

		var filterValue = $filterDropdown.val();
		var filterType = $filterDropdown.attr('data-type');

		// type of the current box (e.g. "query" if we are in the "queries" section)
		var entityType = $activeBox.niceEntityType();

		// Iterate through all the entities in the list and check whether it has the name for field determined
		// by filterType set to inputValue. If so show the element, otherwise hide it.
		$list.find('li').each(function() {
			var $li = $(this);
			var entityName = $li.attr('data-entityname');
			var entity = PocketQuery.getEntity(entityType, entityName);
			if (!filterValue || entity[filterType] === filterValue) {
				$li.removeClass('hidden');
			} else {
				$li.addClass('hidden');
			}
		});
		// check if all is hidden, if so display "nothing found" info, else hide it
		if ($list.find('li:not(.hidden)').length) {
			if($list.hasClass('all-hidden')) {
				$list.removeClass('all-hidden');
			}
			$list.niceStatus('');
		} else {
			$list.addClass('all-hidden');
			$list.niceStatus(getText('admin.filter.empty'));
		}

		// Our li:first-child selector doesn't work anymore now, so we need to apply an additional class for the
		// first visible element.
		handleFirstVisibleListItem($list);
	}

	function initFormTooltips() {
		$('.nice-form .aui-iconfont-help').tooltip({
			title: function() {
				var $target = $(this);
				var $fieldgroup = $target.closest('.field-group');
				return $fieldgroup.find('.description').text();
			},
			gravity: 'n',
			trigger: 'hover'
		});
	}

	function init() {
		$container.find('script').remove();
		$container.closest('#admin-content').find('.logo-heading-block').append(PocketQuery.getHelpLink());
		initFormTooltips();
		initDashboardTooltips();
		$container.addClass('visible');
		// Populate any filter dropdown in the active box initially. This only happens when the page is loaded
		// on a section with a dropdown.
		updateActiveFilterDropdown();
		activateExpandTriggers();
	}

	function bind() {
		$container
			.on('submit', 'form', onFormSubmit)
			.on('click', '.nice-left li', onItemClick)
			.on('click', '.nice-cancel', onCancelClick)
			.on('click', '.nice-remove', onRemoveItemClick)
			.on('click', '.field-group[data-field="cacheduration"] .label-link', onClearCacheClick)
			.on('click', '#nice-form-database .buttons a.testconnection', onTestDatabaseClick)
			.on('click', '.nice-left', onLeftAreaClick)
			.on('focus', '#database-driver', onDatabaseDriverFocus)
			.on('change', '#database-type, #query-database', onTypeSelectChange)
			.on('click', '.nice-add-entity', onAddItemClick)
			.on('change', '.nice-form select', onFormContentChange)
			.on('keyup', '.nice-form input, .nice-form textarea', onFormContentChange)
			.on('click', '.field-group[data-field="content"] .label-link', onContentLabelLinkClick)
			.on('click', '#pocket-dashboard .entity-box', onDashboardBoxClick)
			.on('mouseenter', '.nice-list', onListMouseEnter)
			.on('mouseleave', '.nice-list', onListMouseLeave)
			.on('change', '.nice-filter-dropdown', onFilterDropdownChange);

		$menu.on('click', '.aui-navgroup-primary a', onMenuLinkClick);
	}

	if (PocketQuery.shallUseHistoryInGeneral()) {
		History.Adapter.bind(window, 'statechange', function(e, data) {
			var stateObj = History.getState().data;
			data.popstateEvent.stopImmediatePropagation();
			if (stateObj.origin === 'pocketquery') { // only do something if PQ pushed this state
				PocketQuery.logger.debug('statechange event with data', stateObj);
				doChangeSection(stateObj.section);
			}
		});
	}

	AJS.bind('pocketquery.render.body.end', function() {
		soyAlias = Confluence.Templates.PocketQuery.Admin;
		$body = $(document.body);
		$wrapper = $('#pocketquery-admin-wrapper');
		$container = $('#pocket-admin');
		$menu = $('#pocket-menu');
		$wrapper.prepend(PocketQuery.getHelpLink());
		bind();
		init();
	});

	return {
		storeEntity : storeEntity,
		getEntity : getEntity,
		storeSettings: storeSettings,
		getSettings: getSettings,
		_test: {
			addItemSorted: addItemSorted
		}
	};

}($ || jQuery)));