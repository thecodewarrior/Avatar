if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'Avatar'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'Avatar'.");
}
var Avatar = function (_, Kotlin) {
  'use strict';
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var math = Kotlin.kotlin.math;
  var HashSet_init = Kotlin.kotlin.collections.HashSet_init_287e2$;
  var Enum = Kotlin.kotlin.Enum;
  var throwISE = Kotlin.throwISE;
  var get_indices = Kotlin.kotlin.collections.get_indices_gzk92b$;
  var joinToString = Kotlin.kotlin.collections.joinToString_fmv235$;
  var trimIndent = Kotlin.kotlin.text.trimIndent_pdl1vz$;
  var replace = Kotlin.kotlin.text.replace_r2fvfm$;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var ensureNotNull = Kotlin.ensureNotNull;
  var throwCCE = Kotlin.throwCCE;
  var split = Kotlin.kotlin.text.split_o64adg$;
  var toIntOrNull = Kotlin.kotlin.text.toIntOrNull_pdl1vz$;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var Any = Object;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var Unit = Kotlin.kotlin.Unit;
  var addAll = Kotlin.kotlin.collections.addAll_ye1y7v$;
  var numberToDouble = Kotlin.numberToDouble;
  var hashCode = Kotlin.hashCode;
  var numberToInt = Kotlin.numberToInt;
  var lazy = Kotlin.kotlin.lazy_klfg04$;
  Avatar.prototype = Object.create(AvatarXML.prototype);
  Avatar.prototype.constructor = Avatar;
  AvatarDiskIntake.prototype = Object.create(AvatarXML.prototype);
  AvatarDiskIntake.prototype.constructor = AvatarDiskIntake;
  AvatarJets.prototype = Object.create(AvatarXML.prototype);
  AvatarJets.prototype.constructor = AvatarJets;
  AvatarFeature.prototype = Object.create(Enum.prototype);
  AvatarFeature.prototype.constructor = AvatarFeature;
  Container.prototype = Object.create(AvatarXML.prototype);
  Container.prototype.constructor = Container;
  function main$lambda(it) {
    return AvatarSettingsUI_getInstance();
  }
  function main(args) {
    window.onload = main$lambda;
  }
  function Avatar(settings) {
    AvatarXML.call(this, settings);
    this.blackHoleRadius = 30;
    this.haloSize = 10;
    this.diskRadius = 100;
  }
  Avatar.prototype.gen = function () {
    this.println_61zpoe$(' <g id="avatar" transform="rotate(-45)"> ');
    if (this.settings.features.contains_11rb$(AvatarFeature$HALO_getInstance())) {
      this.println_61zpoe$(' <circle id="halo" cx="0" cy="0" r="40" fill="#FFF"/> ');
    }
    this.println_61zpoe$('<circle id="event-horizon-back" cx="0" cy="0" r="30" fill="#000"/>');
    this.println_61zpoe$('<g transform=' + '"' + 'scale(1 ' + this.settings.heightRatio + ')' + '"' + '>');
    this.println_61zpoe$('<circle id="accretion-disk" cx="0" cy="0" r="100" fill="#FFF"/>');
    if (this.settings.features.contains_11rb$(AvatarFeature$INTAKE_getInstance())) {
      this.println_61zpoe$((new AvatarDiskIntake(this.settings, this.diskRadius)).toString());
    }
    this.println_61zpoe$('<\/g>');
    this.println_61zpoe$('<path id=' + '"' + 'event-horizon-front' + '"' + ' d=' + '"' + '\n' + '            M -' + this.blackHoleRadius + ',0' + '\n' + '            A 1,1 0 0,1 ' + this.blackHoleRadius + ',0' + '\n' + '            A 1,' + this.settings.heightRatio + ' 0 0,1 -' + this.blackHoleRadius + ',0' + '\n' + '        ' + '"' + ' fill=' + '"' + '#000' + '"' + '/>');
    if (this.settings.features.contains_11rb$(AvatarFeature$JETS_getInstance())) {
      this.println_61zpoe$((new AvatarJets(this.settings, this.blackHoleRadius)).toString());
    }
    this.println_61zpoe$('<\/g>');
  };
  Avatar.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Avatar',
    interfaces: [AvatarXML]
  };
  function AvatarDiskIntake(settings, diskMax) {
    AvatarXML.call(this, settings);
    this.diskMax = diskMax;
  }
  var Math_0 = Math;
  AvatarDiskIntake.prototype.gen = function () {
    var path = new PathBuilder();
    var diskMin = this.diskMax - (this.diskMax / 3 | 0) | 0;
    var outerSemiMajorAxis = this.diskMax * 6.0;
    var tmp$ = Math_0.pow(outerSemiMajorAxis, 2);
    var $receiver = outerSemiMajorAxis - this.diskMax;
    var x = tmp$ - Math_0.pow($receiver, 2);
    var outerSemiMinorAxis = Math_0.sqrt(x);
    var innerSemiMajorAxis = this.diskMax * 3.0;
    var tmp$_0 = Math_0.pow(innerSemiMajorAxis, 2);
    var $receiver_0 = innerSemiMajorAxis - diskMin;
    var x_0 = tmp$_0 - Math_0.pow($receiver_0, 2);
    var innerSemiMinorAxis = Math_0.sqrt(x_0);
    path.add_98gap4$('M', [vec(-this.diskMax | 0, 0)]);
    path.add_98gap4$('A', [outerSemiMajorAxis, outerSemiMinorAxis, 0, 0, 1, vec(outerSemiMajorAxis * 2 - this.diskMax, 0)]);
    path.add_98gap4$('L', [vec(innerSemiMajorAxis * 2 - diskMin, 0)]);
    path.add_98gap4$('A', [innerSemiMajorAxis, innerSemiMinorAxis, 0, 0, 0, vec(-diskMin | 0, 0)]);
    path.add_98gap4$('L', [vec(-this.diskMax | 0, 0)]);
    path.add_98gap4$('z', []);
    this.println_61zpoe$('<path d=' + '"' + path + '"' + ' fill=' + '"' + '#fff' + '"' + '/>');
  };
  AvatarDiskIntake.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AvatarDiskIntake',
    interfaces: [AvatarXML]
  };
  function AvatarJets(settings, blackHoleRadius) {
    AvatarXML.call(this, settings);
    this.blackHoleRadius = blackHoleRadius;
    this.innerAngle = math.PI / 6;
    this.jetRadius = 5;
    this.jetSlope = 1 / 1500.0;
    this.jetLength = 1500;
    this.curveEndExtension = 20;
  }
  AvatarJets.prototype.gen = function () {
    var x = this.innerAngle - math.PI / 2;
    var tangentSlope = Math_0.tan(x);
    var tmp$ = this.blackHoleRadius;
    var x_0 = this.innerAngle;
    var tmp$_0 = tmp$ * Math_0.sin(x_0);
    var tmp$_1 = this.blackHoleRadius;
    var x_1 = this.innerAngle;
    var contactPoint = vec(tmp$_0, tmp$_1 * Math_0.cos(x_1));
    var controlPoint = vec(this.jetRadius, tangentSlope * (this.jetRadius - contactPoint.x) + contactPoint.y);
    var controlPointDistance = controlPoint.minus_cqmord$(contactPoint).length();
    var curveEndPoint = vec(this.jetRadius, controlPoint.y + controlPointDistance).plus_cqmord$(vec(this.curveEndExtension * this.jetSlope, this.curveEndExtension));
    if (this.jetSlope !== 0.0) {
      var invSlope = 1 / this.jetSlope;
      var intersectionX = (curveEndPoint.y - invSlope * curveEndPoint.x - (contactPoint.y - tangentSlope * contactPoint.x)) / (tangentSlope - invSlope);
      var intersectionY = tangentSlope * (intersectionX - contactPoint.x) + contactPoint.y;
      controlPoint = vec(intersectionX, intersectionY);
    }
    var jetEndPoint = vec(this.jetLength * this.jetSlope, this.jetLength);
    var top = this.jetPath_x0geqg$(contactPoint, controlPoint, curveEndPoint, jetEndPoint);
    var bottom = this.jetPath_x0geqg$(contactPoint.flipX, controlPoint.flipX, curveEndPoint.flipX, jetEndPoint.flipX);
    this.println_61zpoe$('<g id="astrophysical-jets">');
    this.println_61zpoe$('<path d=' + '"' + top + '"' + ' fill=' + '"' + '#fff' + '"' + '/>');
    this.println_61zpoe$('<path d=' + '"' + bottom + '"' + ' fill=' + '"' + '#fff' + '"' + '/>');
    this.println_61zpoe$('<\/g>');
  };
  AvatarJets.prototype.jetPath_x0geqg$ = function (contactPoint, controlPoint, curveEndPoint, jetEndPoint) {
    var path = new PathBuilder();
    path.add_98gap4$('M', [contactPoint.flipY]);
    path.add_98gap4$('A', [1, this.settings.heightRatio, 0, 0, 0, contactPoint]);
    path.add_98gap4$('Q', [controlPoint, curveEndPoint]);
    path.add_98gap4$('L', [jetEndPoint]);
    path.add_98gap4$('L', [jetEndPoint.flipY]);
    path.add_98gap4$('L', [curveEndPoint.flipY]);
    path.add_98gap4$('Q', [controlPoint.flipY, contactPoint.flipY]);
    return path.build();
  };
  AvatarJets.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AvatarJets',
    interfaces: [AvatarXML]
  };
  function AvatarSettings(viewBox, resolution, features, heightRatio) {
    if (viewBox === void 0)
      viewBox = new Rect2d(vec(-100, -100), vec(100, 100));
    if (resolution === void 0)
      resolution = vec(500, 500);
    if (features === void 0)
      features = HashSet_init();
    if (heightRatio === void 0)
      heightRatio = 2 / 9.0;
    this.viewBox = viewBox;
    this.resolution = resolution;
    this.features = features;
    this.heightRatio = heightRatio;
  }
  AvatarSettings.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AvatarSettings',
    interfaces: []
  };
  AvatarSettings.prototype.component1 = function () {
    return this.viewBox;
  };
  AvatarSettings.prototype.component2 = function () {
    return this.resolution;
  };
  AvatarSettings.prototype.component3 = function () {
    return this.features;
  };
  AvatarSettings.prototype.component4 = function () {
    return this.heightRatio;
  };
  AvatarSettings.prototype.copy_hqpx$ = function (viewBox, resolution, features, heightRatio) {
    return new AvatarSettings(viewBox === void 0 ? this.viewBox : viewBox, resolution === void 0 ? this.resolution : resolution, features === void 0 ? this.features : features, heightRatio === void 0 ? this.heightRatio : heightRatio);
  };
  AvatarSettings.prototype.toString = function () {
    return 'AvatarSettings(viewBox=' + Kotlin.toString(this.viewBox) + (', resolution=' + Kotlin.toString(this.resolution)) + (', features=' + Kotlin.toString(this.features)) + (', heightRatio=' + Kotlin.toString(this.heightRatio)) + ')';
  };
  AvatarSettings.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.viewBox) | 0;
    result = result * 31 + Kotlin.hashCode(this.resolution) | 0;
    result = result * 31 + Kotlin.hashCode(this.features) | 0;
    result = result * 31 + Kotlin.hashCode(this.heightRatio) | 0;
    return result;
  };
  AvatarSettings.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.viewBox, other.viewBox) && Kotlin.equals(this.resolution, other.resolution) && Kotlin.equals(this.features, other.features) && Kotlin.equals(this.heightRatio, other.heightRatio)))));
  };
  function AvatarFeature(name, ordinal, description, default_0) {
    if (default_0 === void 0)
      default_0 = false;
    Enum.call(this);
    this.description = description;
    this.default = default_0;
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function AvatarFeature_initFields() {
    AvatarFeature_initFields = function () {
    };
    AvatarFeature$JETS_instance = new AvatarFeature('JETS', 0, 'Relativistic jets');
    AvatarFeature$HALO_instance = new AvatarFeature('HALO', 1, 'Distorted accretion disk', true);
    AvatarFeature$INTAKE_instance = new AvatarFeature('INTAKE', 2, 'Accretion disk intake');
  }
  var AvatarFeature$JETS_instance;
  function AvatarFeature$JETS_getInstance() {
    AvatarFeature_initFields();
    return AvatarFeature$JETS_instance;
  }
  var AvatarFeature$HALO_instance;
  function AvatarFeature$HALO_getInstance() {
    AvatarFeature_initFields();
    return AvatarFeature$HALO_instance;
  }
  var AvatarFeature$INTAKE_instance;
  function AvatarFeature$INTAKE_getInstance() {
    AvatarFeature_initFields();
    return AvatarFeature$INTAKE_instance;
  }
  AvatarFeature.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AvatarFeature',
    interfaces: [Enum]
  };
  function AvatarFeature$values() {
    return [AvatarFeature$JETS_getInstance(), AvatarFeature$HALO_getInstance(), AvatarFeature$INTAKE_getInstance()];
  }
  AvatarFeature.values = AvatarFeature$values;
  function AvatarFeature$valueOf(name) {
    switch (name) {
      case 'JETS':
        return AvatarFeature$JETS_getInstance();
      case 'HALO':
        return AvatarFeature$HALO_getInstance();
      case 'INTAKE':
        return AvatarFeature$INTAKE_getInstance();
      default:throwISE('No enum constant co.thecodewarrior.avatar.generator.AvatarFeature.' + name);
    }
  }
  AvatarFeature.valueOf_61zpoe$ = AvatarFeature$valueOf;
  function AvatarXML(settings) {
    this.settings = settings;
    this.lines_v5totp$_0 = ArrayList_init();
  }
  AvatarXML.prototype.println_61zpoe$ = function (line) {
    this.lines_v5totp$_0.add_11rb$(this.prepXML_pdl1vz$(line));
  };
  AvatarXML.prototype.print_za3rmp$ = function (str) {
    this.lines_v5totp$_0.set_wxm5ur$(get_indices(this.lines_v5totp$_0).last, this.lines_v5totp$_0.get_za3lpa$(get_indices(this.lines_v5totp$_0).last) + str.toString());
  };
  AvatarXML.prototype.toString = function () {
    this.lines_v5totp$_0.clear();
    this.gen();
    return joinToString(this.lines_v5totp$_0, '\n');
  };
  var Regex_init = Kotlin.kotlin.text.Regex_init_61zpoe$;
  AvatarXML.prototype.prepXML_pdl1vz$ = function ($receiver) {
    var tmp$ = trimIndent($receiver);
    return replace(Regex_init('\\\\\n\\s*').replace_x2uqeu$(tmp$, ''), 10, 32);
  };
  AvatarXML.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AvatarXML',
    interfaces: []
  };
  function Container(settings) {
    AvatarXML.call(this, settings);
    this.avatar = new Avatar(settings);
  }
  Container.prototype.gen = function () {
    this.println_61zpoe$('');
    this.println_61zpoe$(this.prepXML_pdl1vz$('\n' + '<svg id=' + '"' + 'main' + '"' + ' version=' + '"' + '1.1' + '"' + ' xmlns=' + '"' + 'http://www.w3.org/2000/svg' + '"' + ' xmlns:xlink=' + '"' + 'http://www.w3.org/1999/xlink' + '"' + ' ' + '\\' + '\n' + 'x=' + '"' + '0px' + '"' + ' y=' + '"' + '0px' + '"' + ' width=' + '"' + this.settings.resolution.x + 'px' + '"' + ' height=' + '"' + this.settings.resolution.y + 'px' + '"' + ' ' + '\\' + '\n' + 'viewBox=' + '"' + this.settings.viewBox.min.x + ' ' + this.settings.viewBox.min.y + ' ' + this.settings.viewBox.width + ' ' + this.settings.viewBox.height + '"' + ' ' + '\\' + '\n' + 'xml:space=' + '"' + 'preserve' + '"' + '>' + '\n' + '                '));
    this.println_61zpoe$(this.avatar.toString());
    this.println_61zpoe$('<\/svg>');
  };
  Container.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Container',
    interfaces: [AvatarXML]
  };
  function AvatarSettingsUI() {
    AvatarSettingsUI_instance = this;
    this.settings = new AvatarSettings();
    AvatarSettingsUI$Bounds_getInstance();
    AvatarSettingsUI$Features_getInstance();
    jq('#load-preview').click(AvatarSettingsUI_init$lambda(this));
  }
  AvatarSettingsUI.prototype.loadPreview_0 = function () {
    var container = ensureNotNull(jq('#preview')[0]);
    container.innerHTML = (new Container(this.settings)).toString();
  };
  function AvatarSettingsUI$Bounds() {
    AvatarSettingsUI$Bounds_instance = this;
    this.presetField_0 = jq('#bounds-preset');
    this.xField_0 = jq('#bounds-x');
    this.yField_0 = jq('#bounds-y');
    this.resolutionField_0 = jq('#resolution');
    this.selectBounds_0();
    this.readBounds_0();
    this.presetField_0.change(AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda(this));
    this.xField_0.change(AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda_0(this));
    this.yField_0.change(AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda_1(this));
    this.resolutionField_0.change(AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda_2(this));
  }
  var wrapFunction = Kotlin.wrapFunction;
  var mapNotNullTo$lambda = wrapFunction(function () {
    return function (closure$transform, closure$destination) {
      return function (element) {
        var tmp$;
        if ((tmp$ = closure$transform(element)) != null) {
          closure$destination.add_11rb$(tmp$);
        }
        return Unit;
      };
    };
  });
  AvatarSettingsUI$Bounds.prototype.selectBounds_0 = function () {
    var tmp$;
    var $receiver = split(typeof (tmp$ = get_value(this.presetField_0)) === 'string' ? tmp$ : throwCCE(), Kotlin.charArrayOf(32));
    var destination = ArrayList_init();
    var tmp$_0;
    tmp$_0 = $receiver.iterator();
    while (tmp$_0.hasNext()) {
      var element = tmp$_0.next();
      var tmp$_0_0;
      if ((tmp$_0_0 = toIntOrNull(element)) != null) {
        destination.add_11rb$(tmp$_0_0);
      }
    }
    var selected = destination;
    if (selected.size === 3) {
      value_0(this.xField_0, selected.get_za3lpa$(0));
      value_0(this.yField_0, selected.get_za3lpa$(1));
      value_0(this.resolutionField_0, selected.get_za3lpa$(2));
    }
  };
  AvatarSettingsUI$Bounds.prototype.readBounds_0 = function () {
    var tmp$, tmp$_0, tmp$_1, tmp$_2, tmp$_3, tmp$_4;
    var x = (tmp$_0 = toIntOrNull(typeof (tmp$ = get_value(this.xField_0)) === 'string' ? tmp$ : throwCCE())) != null ? tmp$_0 : 100;
    var y = (tmp$_2 = toIntOrNull(typeof (tmp$_1 = get_value(this.yField_0)) === 'string' ? tmp$_1 : throwCCE())) != null ? tmp$_2 : 100;
    var resolution = (tmp$_4 = toIntOrNull(typeof (tmp$_3 = get_value(this.resolutionField_0)) === 'string' ? tmp$_3 : throwCCE())) != null ? tmp$_4 : 500;
    AvatarSettingsUI_getInstance().settings.viewBox = new Rect2d(vec(-x | 0, -y | 0), vec(x, y));
    if (x === y)
      AvatarSettingsUI_getInstance().settings.resolution = vec(resolution, resolution);
    else if (x > y)
      AvatarSettingsUI_getInstance().settings.resolution = vec(resolution, Kotlin.imul(y, resolution) / x | 0);
    else if (x < y)
      AvatarSettingsUI_getInstance().settings.resolution = vec(Kotlin.imul(x, resolution) / y | 0, resolution);
    println('Updated bounds: x: \xB1' + x + ' y: \xB1' + y);
    println('Updated resolution: x: ' + AvatarSettingsUI_getInstance().settings.resolution.xi + ' y: ' + '$' + AvatarSettingsUI_getInstance().settings.resolution.yi);
  };
  function AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda(this$Bounds) {
    return function (it) {
      this$Bounds.selectBounds_0();
      this$Bounds.readBounds_0();
      return new Any();
    };
  }
  function AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda_0(this$Bounds) {
    return function (it) {
      this$Bounds.readBounds_0();
      return value(this$Bounds.presetField_0, '');
    };
  }
  function AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda_1(this$Bounds) {
    return function (it) {
      this$Bounds.readBounds_0();
      return value(this$Bounds.presetField_0, '');
    };
  }
  function AvatarSettingsUI$AvatarSettingsUI$Bounds_init$lambda_2(this$Bounds) {
    return function (it) {
      this$Bounds.readBounds_0();
      return value(this$Bounds.presetField_0, '');
    };
  }
  AvatarSettingsUI$Bounds.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Bounds',
    interfaces: []
  };
  var AvatarSettingsUI$Bounds_instance = null;
  function AvatarSettingsUI$Bounds_getInstance() {
    if (AvatarSettingsUI$Bounds_instance === null) {
      new AvatarSettingsUI$Bounds();
    }
    return AvatarSettingsUI$Bounds_instance;
  }
  function AvatarSettingsUI$Features() {
    AvatarSettingsUI$Features_instance = this;
    this.featuresSet_0 = jq('#features-set');
    var $receiver = AvatarFeature$values();
    var tmp$;
    for (tmp$ = 0; tmp$ !== $receiver.length; ++tmp$) {
      var element = $receiver[tmp$];
      this.addFeature_0(element);
    }
  }
  function AvatarSettingsUI$Features$addFeature$lambda(closure$checkbox, closure$feature) {
    return function (it) {
      return closure$checkbox.is(':checked') ? AvatarSettingsUI_getInstance().settings.features.add_11rb$(closure$feature) : AvatarSettingsUI_getInstance().settings.features.remove_11rb$(closure$feature);
    };
  }
  AvatarSettingsUI$Features.prototype.addFeature_0 = function (feature) {
    var tmp$;
    this.featuresSet_0.append(Kotlin.isType(tmp$ = jq.parseHTML(trimIndent('\n' + '                <div>' + '\n' + '                    <input type=' + '"' + 'checkbox' + '"' + ' id=' + '"' + 'feature-' + feature.name + '"' + ' ' + (feature.default ? 'checked' : '') + '>' + '\n' + '                    <label for=' + '"' + 'feature-' + feature.name + '"' + '>' + feature.description + '<\/label>' + '\n' + '                <\/div>' + '\n' + '            '))[0], Object) ? tmp$ : throwCCE());
    if (feature.default)
      AvatarSettingsUI_getInstance().settings.features.add_11rb$(feature);
    var checkbox = jq('#feature-' + feature.name);
    checkbox.change(AvatarSettingsUI$Features$addFeature$lambda(checkbox, feature));
  };
  AvatarSettingsUI$Features.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Features',
    interfaces: []
  };
  var AvatarSettingsUI$Features_instance = null;
  function AvatarSettingsUI$Features_getInstance() {
    if (AvatarSettingsUI$Features_instance === null) {
      new AvatarSettingsUI$Features();
    }
    return AvatarSettingsUI$Features_instance;
  }
  function AvatarSettingsUI_init$lambda(this$AvatarSettingsUI) {
    return function (it) {
      this$AvatarSettingsUI.loadPreview_0();
      return Unit;
    };
  }
  AvatarSettingsUI.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'AvatarSettingsUI',
    interfaces: []
  };
  var AvatarSettingsUI_instance = null;
  function AvatarSettingsUI_getInstance() {
    if (AvatarSettingsUI_instance === null) {
      new AvatarSettingsUI();
    }
    return AvatarSettingsUI_instance;
  }
  function PathBuilder() {
    this.path_0 = ArrayList_init();
  }
  PathBuilder.prototype.add_98gap4$ = function (command, args) {
    this.path_0.add_11rb$(command);
    addAll(this.path_0, args);
  };
  function PathBuilder$build$lambda(it) {
    var tmp$, tmp$_0, tmp$_1;
    return (tmp$_1 = (tmp$_0 = Kotlin.isType(tmp$ = it, Vec2d) ? tmp$ : null) != null ? tmp$_0.x.toString() + ',' + tmp$_0.y : null) != null ? tmp$_1 : it.toString();
  }
  PathBuilder.prototype.build = function () {
    return joinToString(this.path_0, ' ', void 0, void 0, void 0, void 0, PathBuilder$build$lambda);
  };
  PathBuilder.prototype.toString = function () {
    return this.build();
  };
  PathBuilder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'PathBuilder',
    interfaces: []
  };
  function Rect2d(min, max) {
    var a = min.x;
    var b = max.x;
    var tmp$ = Math_0.min(a, b);
    var a_0 = min.y;
    var b_0 = max.y;
    this.min = vec(tmp$, Math_0.min(a_0, b_0));
    var a_1 = min.x;
    var b_1 = max.x;
    var tmp$_0 = Math_0.max(a_1, b_1);
    var a_2 = min.y;
    var b_2 = max.y;
    this.max = vec(tmp$_0, Math_0.max(a_2, b_2));
  }
  Object.defineProperty(Rect2d.prototype, 'width', {
    get: function () {
      return this.max.x - this.min.x;
    }
  });
  Object.defineProperty(Rect2d.prototype, 'height', {
    get: function () {
      return this.max.y - this.min.y;
    }
  });
  Rect2d.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Rect2d',
    interfaces: []
  };
  var jq;
  function get_value($receiver) {
    return $receiver.val();
  }
  function value($receiver, value) {
    return $receiver.val(value);
  }
  function value_0($receiver, value) {
    return $receiver.val(value);
  }
  function value_1($receiver, value) {
    return $receiver.val(value);
  }
  function value_2($receiver, func) {
    return $receiver.val(func);
  }
  function Vec2d(x, y) {
    Vec2d$Companion_getInstance();
    this.x = x;
    this.y = y;
    this.xf = this.x;
    this.yf = this.y;
    var x_0 = this.x;
    this.xi = numberToInt(Math_0.floor(x_0));
    var x_1 = this.y;
    this.yi = numberToInt(Math_0.floor(x_1));
    this.len_l8bdg4$_0 = lazy(Vec2d$len$lambda(this));
  }
  Vec2d.prototype.floor = function () {
    var x = this.x;
    var tmp$ = Math_0.floor(x);
    var x_0 = this.y;
    return new Vec2d(tmp$, Math_0.floor(x_0));
  };
  Vec2d.prototype.ceil = function () {
    var x = this.x;
    var tmp$ = Math_0.ceil(x);
    var x_0 = this.y;
    return new Vec2d(tmp$, Math_0.ceil(x_0));
  };
  Vec2d.prototype.setX_14dthe$ = function (value) {
    return new Vec2d(value, this.y);
  };
  Vec2d.prototype.setY_14dthe$ = function (value) {
    return new Vec2d(this.x, value);
  };
  Vec2d.prototype.add_cqmord$ = function (other) {
    return new Vec2d(this.x + other.x, this.y + other.y);
  };
  Vec2d.prototype.add_lu1900$ = function (otherX, otherY) {
    return new Vec2d(this.x + otherX, this.y + otherY);
  };
  Vec2d.prototype.sub_cqmord$ = function (other) {
    return new Vec2d(this.x - other.x, this.y - other.y);
  };
  Vec2d.prototype.sub_lu1900$ = function (otherX, otherY) {
    return new Vec2d(this.x - otherX, this.y - otherY);
  };
  Vec2d.prototype.mul_cqmord$ = function (other) {
    return new Vec2d(this.x * other.x, this.y * other.y);
  };
  Vec2d.prototype.mul_lu1900$ = function (otherX, otherY) {
    return new Vec2d(this.x * otherX, this.y * otherY);
  };
  Vec2d.prototype.mul_14dthe$ = function (amount) {
    return new Vec2d(this.x * amount, this.y * amount);
  };
  Vec2d.prototype.divide_cqmord$ = function (other) {
    return new Vec2d(this.x / other.x, this.y / other.y);
  };
  Vec2d.prototype.divide_lu1900$ = function (otherX, otherY) {
    return new Vec2d(this.x / otherX, this.y / otherY);
  };
  Vec2d.prototype.divide_14dthe$ = function (amount) {
    return new Vec2d(this.x / amount, this.y / amount);
  };
  Vec2d.prototype.dot_cqmord$ = function (point) {
    return this.x * point.x + this.y * point.y;
  };
  Object.defineProperty(Vec2d.prototype, 'len_0', {
    get: function () {
      return this.len_l8bdg4$_0.value;
    }
  });
  Vec2d.prototype.length = function () {
    return this.len_0;
  };
  Vec2d.prototype.normalize = function () {
    var norm = this.length();
    return new Vec2d(this.x / norm, this.y / norm);
  };
  Vec2d.prototype.squareDist_cqmord$ = function (vec) {
    var d0 = vec.x - this.x;
    var d1 = vec.y - this.y;
    return d0 * d0 + d1 * d1;
  };
  Vec2d.prototype.projectOnTo_cqmord$ = function (other) {
    var norm = other.normalize();
    return norm.mul_14dthe$(this.dot_cqmord$(norm));
  };
  Vec2d.prototype.rotate_3p81yu$ = function (theta) {
    var x = numberToDouble(theta);
    var cs = Math_0.cos(x);
    var x_0 = numberToDouble(theta);
    var sn = Math_0.sin(x_0);
    return new Vec2d(this.x * cs - this.y * sn, this.x * sn + this.y * cs);
  };
  Object.defineProperty(Vec2d.prototype, 'flipX', {
    get: function () {
      return new Vec2d(this.x, -this.y);
    }
  });
  Object.defineProperty(Vec2d.prototype, 'flipY', {
    get: function () {
      return new Vec2d(-this.x, this.y);
    }
  });
  Vec2d.prototype.hashCode = function () {
    var prime = 31;
    var result = 1;
    result = Kotlin.imul(prime, result) + hashCode(this.x) | 0;
    result = Kotlin.imul(prime, result) + hashCode(this.y) | 0;
    return result;
  };
  Vec2d.prototype.equals = function (other) {
    var tmp$;
    if (this === other)
      return true;
    if (other == null)
      return false;
    return this.x === (Kotlin.isType(tmp$ = other, Vec2d) ? tmp$ : throwCCE()).x && this.y === other.y;
  };
  Vec2d.prototype.toString = function () {
    return '(' + this.x + ',' + this.y + ')';
  };
  Vec2d.prototype.times_cqmord$ = function (other) {
    return this.mul_cqmord$(other);
  };
  Vec2d.prototype.times_14dthe$ = function (other) {
    return this.mul_14dthe$(other);
  };
  Vec2d.prototype.times_mx4ult$ = function (other) {
    return this.times_14dthe$(other);
  };
  Vec2d.prototype.times_za3lpa$ = function (other) {
    return this.times_14dthe$(other);
  };
  Vec2d.prototype.div_cqmord$ = function (other) {
    return this.divide_cqmord$(other);
  };
  Vec2d.prototype.div_14dthe$ = function (other) {
    return this.divide_14dthe$(other);
  };
  Vec2d.prototype.div_mx4ult$ = function (other) {
    return this.div_14dthe$(other);
  };
  Vec2d.prototype.div_za3lpa$ = function (other) {
    return this.div_14dthe$(other);
  };
  Vec2d.prototype.plus_cqmord$ = function (other) {
    return this.add_cqmord$(other);
  };
  Vec2d.prototype.minus_cqmord$ = function (other) {
    return this.sub_cqmord$(other);
  };
  Vec2d.prototype.unaryMinus = function () {
    return this.times_za3lpa$(-1);
  };
  Vec2d.prototype.component1 = function () {
    return this.x;
  };
  Vec2d.prototype.component2 = function () {
    return this.y;
  };
  function Vec2d$Companion() {
    Vec2d$Companion_instance = this;
    this.ZERO = new Vec2d(0.0, 0.0);
  }
  Vec2d$Companion.prototype.min_njtwim$ = function (a, b) {
    var a_0 = a.x;
    var b_0 = b.x;
    var tmp$ = Math_0.min(a_0, b_0);
    var a_1 = a.y;
    var b_1 = b.y;
    return new Vec2d(tmp$, Math_0.min(a_1, b_1));
  };
  Vec2d$Companion.prototype.max_njtwim$ = function (a, b) {
    var a_0 = a.x;
    var b_0 = b.x;
    var tmp$ = Math_0.max(a_0, b_0);
    var a_1 = a.y;
    var b_1 = b.y;
    return new Vec2d(tmp$, Math_0.max(a_1, b_1));
  };
  Vec2d$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Vec2d$Companion_instance = null;
  function Vec2d$Companion_getInstance() {
    if (Vec2d$Companion_instance === null) {
      new Vec2d$Companion();
    }
    return Vec2d$Companion_instance;
  }
  function Vec2d$len$lambda(this$Vec2d) {
    return function () {
      var x = this$Vec2d.x * this$Vec2d.x + this$Vec2d.y * this$Vec2d.y;
      return Math_0.sqrt(x);
    };
  }
  Vec2d.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Vec2d',
    interfaces: []
  };
  function vec(x, y) {
    return new Vec2d(numberToDouble(x), numberToDouble(y));
  }
  var package$co = _.co || (_.co = {});
  var package$thecodewarrior = package$co.thecodewarrior || (package$co.thecodewarrior = {});
  var package$avatar = package$thecodewarrior.avatar || (package$thecodewarrior.avatar = {});
  package$avatar.main_kand9s$ = main;
  var package$generator = package$avatar.generator || (package$avatar.generator = {});
  package$generator.Avatar = Avatar;
  package$generator.AvatarDiskIntake = AvatarDiskIntake;
  package$generator.AvatarJets = AvatarJets;
  package$generator.AvatarSettings = AvatarSettings;
  Object.defineProperty(AvatarFeature, 'JETS', {
    get: AvatarFeature$JETS_getInstance
  });
  Object.defineProperty(AvatarFeature, 'HALO', {
    get: AvatarFeature$HALO_getInstance
  });
  Object.defineProperty(AvatarFeature, 'INTAKE', {
    get: AvatarFeature$INTAKE_getInstance
  });
  package$generator.AvatarFeature = AvatarFeature;
  package$generator.AvatarXML = AvatarXML;
  package$generator.Container = Container;
  var package$ui = package$avatar.ui || (package$avatar.ui = {});
  Object.defineProperty(package$ui, 'AvatarSettingsUI', {
    get: AvatarSettingsUI_getInstance
  });
  var package$util = package$avatar.util || (package$avatar.util = {});
  package$util.PathBuilder = PathBuilder;
  package$util.Rect2d = Rect2d;
  Object.defineProperty(package$util, 'jq', {
    get: function () {
      return jq;
    }
  });
  package$util.get_value_a5lmlr$ = get_value;
  package$util.value_btepzh$ = value;
  package$util.value_9gn29x$ = value_0;
  package$util.value_li8lj3$ = value_1;
  package$util.value_1q162z$ = value_2;
  Object.defineProperty(Vec2d, 'Companion', {
    get: Vec2d$Companion_getInstance
  });
  package$util.Vec2d = Vec2d;
  package$util.vec_z8e4lc$ = vec;
  jq = jQuery;
  main([]);
  Kotlin.defineModule('Avatar', _);
  return _;
}(typeof Avatar === 'undefined' ? {} : Avatar, kotlin);

//# sourceMappingURL=Avatar.js.map
