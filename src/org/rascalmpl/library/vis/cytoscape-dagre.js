(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("dagre"));
	else if(typeof define === 'function' && define.amd)
		define(["dagre"], factory);
	else if(typeof exports === 'object')
		exports["cytoscapeDagre"] = factory(require("dagre"));
	else
		root["cytoscapeDagre"] = factory(root["dagre"]);
})(this, function(__WEBPACK_EXTERNAL_MODULE__4__) {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

var impl = __webpack_require__(1);

// registers the extension on a cytoscape lib ref
var register = function register(cytoscape) {
  if (!cytoscape) {
    return;
  } // can't register if cytoscape unspecified

  cytoscape('layout', 'dagre', impl); // register with cytoscape.js
};
if (typeof cytoscape !== 'undefined') {
  // expose to global cytoscape (i.e. window.cytoscape)
  register(cytoscape);
}
module.exports = register;

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

function _typeof(o) { "@babel/helpers - typeof"; return _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (o) { return typeof o; } : function (o) { return o && "function" == typeof Symbol && o.constructor === Symbol && o !== Symbol.prototype ? "symbol" : typeof o; }, _typeof(o); }
function _createForOfIteratorHelper(r, e) { var t = "undefined" != typeof Symbol && r[Symbol.iterator] || r["@@iterator"]; if (!t) { if (Array.isArray(r) || (t = _unsupportedIterableToArray(r)) || e && r && "number" == typeof r.length) { t && (r = t); var _n = 0, F = function F() {}; return { s: F, n: function n() { return _n >= r.length ? { done: !0 } : { done: !1, value: r[_n++] }; }, e: function e(r) { throw r; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); } var o, a = !0, u = !1; return { s: function s() { t = t.call(r); }, n: function n() { var r = t.next(); return a = r.done, r; }, e: function e(r) { u = !0, o = r; }, f: function f() { try { a || null == t["return"] || t["return"](); } finally { if (u) throw o; } } }; }
function _unsupportedIterableToArray(r, a) { if (r) { if ("string" == typeof r) return _arrayLikeToArray(r, a); var t = {}.toString.call(r).slice(8, -1); return "Object" === t && r.constructor && (t = r.constructor.name), "Map" === t || "Set" === t ? Array.from(r) : "Arguments" === t || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(t) ? _arrayLikeToArray(r, a) : void 0; } }
function _arrayLikeToArray(r, a) { (null == a || a > r.length) && (a = r.length); for (var e = 0, n = Array(a); e < a; e++) { n[e] = r[e]; } return n; }
var isFunction = function isFunction(o) {
  return typeof o === 'function';
};
var defaults = __webpack_require__(2);
var assign = __webpack_require__(3);
var dagre = __webpack_require__(4);
var EPSILON = 0.001; // what does it mean to be too close to 0?

// constructor
// options : object containing layout options
function DagreLayout(options) {
  this.options = assign({}, defaults, options);
}
function subtract(a, b) {
  return {
    x: noZero(a.x - b.x),
    y: noZero(a.y - b.y)
  };
}
function product(a, b) {
  return noZero(a.x * b.x) + noZero(a.y * b.y);
}
function norm(v) {
  var len = Math.hypot(v.x, v.y) || 1;
  return {
    x: v.x / len,
    y: v.y / len,
    len: len
  };
}
function perp(v) {
  return {
    x: -v.y,
    y: v.x
  };
}

/* provides the context for mapping from dagre's x, y coordinate system
 * for control points to cytoscapes coordinate system for control points
 * which is relative to the straight vector from source to target node
 */
function buildEdgeFrame(src, tgt) {
  var d = subtract(tgt, src);
  var _norm = norm(d),
    x = _norm.x,
    y = _norm.y,
    len = _norm.len;
  var dir = {
    x: x,
    y: y
  };
  var normal = perp(dir);
  return {
    src: src,
    tgt: tgt,
    dir: dir,
    normal: normal,
    len: len
  };
}
function noZero(x) {
  if (Math.abs(x) < EPSILON) {
    return x < 0 ? -EPSILON : EPSILON;
  }
  return x;
}
function toEdgeCoordinates(P, frame) {
  var vector = subtract(P, frame.src);
  var weight = noZero(product(vector, frame.dir) / frame.len);
  var distance = noZero(product(vector, frame.normal));
  return {
    weight: weight,
    distance: distance
  };
}
function normalizeWeight(coords) {
  var min = Infinity;
  var max = -Infinity;
  var _iterator = _createForOfIteratorHelper(coords),
    _step;
  try {
    for (_iterator.s(); !(_step = _iterator.n()).done;) {
      var p = _step.value;
      if (p.weight < min) {
        min = p.weight;
      }
      if (p.weight > max) {
        max = p.weight;
      }
    }
  } catch (err) {
    _iterator.e(err);
  } finally {
    _iterator.f();
  }
  var range = max - min || 1;
  return coords.map(function (p) {
    return {
      distance: p.distance,
      weight: (p.weight - min) / range
    };
  });
}

/* First introduce new control points to bridge between the dagre list of 
 * points and the centres of cytoscape nodes.
 * Then we sanitize any empty or non-existing or degenerate control points
 * And finally we map the Dagre coordinates to the Cytoscape coordinated which
 * are relative to the original direction vector from source to target.
 * These final coordinates are stored pairwise in two arrays cpw and cpd
 * which are picked up by the Bezier construction code in cytoscape.
 */
function dagreEdgeToCytoscapeEdge(dEdge, cEdge) {
  var fromNode = cEdge.source().position();
  var toNode = cEdge.target().position();
  var frame = buildEdgeFrame(fromNode, toNode);
  var coords = normalizeWeight(dEdge.points.map(function (p) {
    return toEdgeCoordinates(p, frame);
  }));
  var controlPointWeights = coords.slice(1, -1).map(function (c) {
    return c.weight;
  });
  var controlPointDistances = coords.slice(1, -1).map(function (c) {
    return c.distance;
  });
  var result = {
    controlPointWeights: controlPointWeights,
    controlPointDistances: controlPointDistances
  };
  return result;
}

// runs the layout
DagreLayout.prototype.run = function () {
  var options = this.options;
  var layout = this;
  var cy = options.cy; // cy is automatically populated for us in the constructor
  var eles = options.eles;
  var getVal = function getVal(ele, val) {
    return isFunction(val) ? val.apply(ele, [ele]) : val;
  };
  var bb = options.boundingBox || {
    x1: 0,
    y1: 0,
    w: cy.width(),
    h: cy.height()
  };
  if (bb.x2 === undefined) {
    bb.x2 = bb.x1 + bb.w;
  }
  if (bb.w === undefined) {
    bb.w = bb.x2 - bb.x1;
  }
  if (bb.y2 === undefined) {
    bb.y2 = bb.y1 + bb.h;
  }
  if (bb.h === undefined) {
    bb.h = bb.y2 - bb.y1;
  }
  var g = new dagre.graphlib.Graph({
    multigraph: true,
    compound: true
  });
  var gObj = {};
  var setGObj = function setGObj(name, val) {
    if (val != null) {
      gObj[name] = val;
    }
  };
  setGObj('nodesep', options.nodeSep);
  setGObj('edgesep', options.edgeSep);
  setGObj('ranksep', options.rankSep);
  setGObj('rankdir', options.rankDir);
  setGObj('align', options.align);
  setGObj('ranker', options.ranker);
  setGObj('acyclicer', options.acyclicer);
  g.setGraph(gObj);
  g.setDefaultEdgeLabel(function () {
    return {};
  });
  g.setDefaultNodeLabel(function () {
    return {};
  });

  // add nodes to dagre
  var nodes = eles.nodes();
  if (isFunction(options.sort)) {
    nodes = nodes.sort(options.sort);
  }
  for (var i = 0; i < nodes.length; i++) {
    var node = nodes[i];
    var nbb = node.layoutDimensions(options);
    g.setNode(node.id(), {
      width: nbb.w,
      height: nbb.h,
      shape: 'ellipse',
      name: node.id()
    });
  }

  // set compound parents
  for (var _i = 0; _i < nodes.length; _i++) {
    var _node = nodes[_i];
    if (_node.isChild()) {
      g.setParent(_node.id(), _node.parent().id());
    }
  }

  // add edges to dagre
  var edges = eles.edges().stdFilter(function (edge) {
    return !edge.source().isParent() && !edge.target().isParent(); // dagre can't handle edges on compound nodes
  });
  if (isFunction(options.sort)) {
    edges = edges.sort(options.sort);
  }
  for (var _i2 = 0; _i2 < edges.length; _i2++) {
    var edge = edges[_i2];
    g.setEdge(edge.source().id(), edge.target().id(), {
      minlen: getVal(edge, options.minLen),
      weight: getVal(edge, options.edgeWeight),
      name: edge.id()
    }, edge.id());
  }
  dagre.layout(g);
  var gNodeIds = g.nodes();
  for (var _i3 = 0; _i3 < gNodeIds.length; _i3++) {
    var id = gNodeIds[_i3];
    var n = g.node(id);
    cy.getElementById(id).scratch().dagre = n;
  }
  var dagreBB;
  if (options.boundingBox) {
    dagreBB = {
      x1: Infinity,
      x2: -Infinity,
      y1: Infinity,
      y2: -Infinity
    };
    nodes.forEach(function (node) {
      var dModel = node.scratch().dagre;
      dagreBB.x1 = Math.min(dagreBB.x1, dModel.x);
      dagreBB.x2 = Math.max(dagreBB.x2, dModel.x);
      dagreBB.y1 = Math.min(dagreBB.y1, dModel.y);
      dagreBB.y2 = Math.max(dagreBB.y2, dModel.y);
    });
    dagreBB.w = dagreBB.x2 - dagreBB.x1;
    dagreBB.h = dagreBB.y2 - dagreBB.y1;
  } else {
    dagreBB = bb;
  }
  var constrainPos = function constrainPos(p) {
    if (options.boundingBox) {
      var xPct = dagreBB.w === 0 ? 0 : (p.x - dagreBB.x1) / dagreBB.w;
      var yPct = dagreBB.h === 0 ? 0 : (p.y - dagreBB.y1) / dagreBB.h;
      return {
        x: bb.x1 + xPct * bb.w,
        y: bb.y1 + yPct * bb.h
      };
    } else {
      return p;
    }
  };
  nodes.layoutPositions(layout, options, function (ele) {
    ele = _typeof(ele) === "object" ? ele : this;
    var dModel = ele.scratch().dagre;
    return constrainPos({
      x: dModel.x,
      y: dModel.y
    });
  });
  if (options.useDagreEdgeControlPoints) {
    if (options.automaticDagreEdgeStyle) {
      cy.edges().addClass('useDagreEdgeControlPoints');
      cy.style().selector('edge.useDagreEdgeControlPoints').style(options.getDagreEdgeStyle()).update();
    }
    g.edges().forEach(function (id) {
      var cyEdge = cy.getElementById(id.name);
      var dEdge = g.edge(id);
      if (dEdge && dEdge.points) {
        cyEdge.scratch(dagreEdgeToCytoscapeEdge(dEdge, cyEdge));
      }
    });
  }
  return this; // chaining
};
module.exports = DagreLayout;

/***/ }),
/* 2 */
/***/ (function(module, exports) {

var defaults = {
  // dagre algo options, uses default value on undefined
  nodeSep: undefined,
  // the separation between adjacent nodes in the same rank
  edgeSep: undefined,
  // the separation between adjacent edges in the same rank
  rankSep: undefined,
  // the separation between adjacent nodes in the same rank
  rankDir: undefined,
  // 'TB' for top to bottom flow, 'LR' for left to right,
  align: undefined,
  // alignment for rank nodes. Can be 'UL', 'UR', 'DL', or 'DR', where U = up, D = down, L = left, and R = right
  acyclicer: undefined,
  // If set to 'greedy', uses a greedy heuristic for finding a feedback arc set for a graph.
  // A feedback arc set is a set of edges that can be removed to make a graph acyclic.
  ranker: undefined,
  // Type of algorithm to assigns a rank to each node in the input graph.
  // Possible values: network-simplex, tight-tree or longest-path
  minLen: function minLen(_edge) {
    return 1;
  },
  // number of ranks to keep between the source and target of the edge
  edgeWeight: function edgeWeight(_edge) {
    return 1;
  },
  // higher weight edges are generally made shorter and straighter than lower weight edges

  // general layout options
  fit: true,
  // whether to fit to viewport
  padding: 30,
  // fit padding
  spacingFactor: undefined,
  // Applies a multiplicative factor (>0) to expand or compress the overall area that the nodes take up
  nodeDimensionsIncludeLabels: false,
  // whether labels should be included in determining the space used by a node
  useDagreEdgeControlPoints: false,
  // enable bezier curves using dagre control points
  /**
   * Automatically adds edge class '.useDagreEdgeControlPoints' to all edges and configure it with this.dagreEdgeStyle.
   * If set to `false` and `useDagreEdgeControlPoints` is `true` then apply `this.dagreEdgeStyle` yourself.
   */
  automaticDagreEdgeStyle: this.useDagreEdgeControlPoints,
  /**
   * Defines the style for rendering dagre edge control points stored by the layout algorithm
   * if `useDagreEdgeControlPoints` is `true` and `automaticDagreEdgeStyle` is `true`
   */
  dagreEdgeStyle: {
    'curve-style': 'unbundled-bezier',
    'control-point-weights': function controlPointWeights(ele) {
      return ele.scratch('controlPointWeights');
    },
    'control-point-distances': function controlPointDistances(ele) {
      return ele.scratch('controlPointDistances');
    },
    'edge-distances': 'intersection',
    'edge-ends-overlap': false
  },
  animate: false,
  // whether to transition the node positions
  animateFilter: function animateFilter(_node, i) {
    return true;
  },
  // whether to animate specific nodes when animation is on; non-animated nodes immediately go to their final positions
  animationDuration: 500,
  // duration of animation in ms if enabled
  animationEasing: undefined,
  // easing of animation if enabled
  boundingBox: undefined,
  // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
  transform: function transform(node, pos) {
    return pos;
  },
  // a function that applies a transform to the final node position
  ready: function ready() {},
  // on layoutready
  sort: undefined,
  // a sorting function to order the nodes and edges; e.g. function(a, b){ return a.data('weight') - b.data('weight') }
  // because cytoscape dagre creates a directed graph, and directed graphs use the node order as a tie breaker when
  // defining the topology of a graph, this sort function can help ensure the correct order of the nodes/edges.
  // this feature is most useful when adding and removing the same nodes and edges multiple times in a graph.
  stop: function stop() {} // on layoutstop
};
module.exports = defaults;

/***/ }),
/* 3 */
/***/ (function(module, exports) {

// Simple, internal Object.assign() polyfill for options objects etc.

module.exports = Object.assign != null ? Object.assign.bind(Object) : function (tgt) {
  for (var _len = arguments.length, srcs = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
    srcs[_key - 1] = arguments[_key];
  }
  srcs.forEach(function (src) {
    Object.keys(src).forEach(function (k) {
      return tgt[k] = src[k];
    });
  });
  return tgt;
};

/***/ }),
/* 4 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE__4__;

/***/ })
/******/ ]);
});