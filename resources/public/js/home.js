// The SVG decision graph on the home page.
$(document).ready(function () {
  // Create a new directed graph
  var g = new dagreD3.graphlib.Graph().setGraph({});
  var happy_style = "stroke: green; stroke-width: 4px; fill: green;"

  var happy_node_style = "fill: #ccc";
  var happy_node_label_style = "font-size: 60px; color: green";
  var happy_label_style = "font-size: 45px";

  g.setNode("start", { style: happy_node_style, label: "Start here", labelStyle: happy_node_label_style});
  g.setEdge("start", "have-some-software", {style: happy_style,
            label: "I have one!",
            labelStyle: happy_label_style});

  g.setNode("have-some-software", { style: happy_node_style, labelStyle: happy_node_label_style, label: "Do you have some software?"});

  g.setNode("want-the-cloud", { style: happy_node_style, labelStyle: happy_label_style, label: "Want it managed by the cloud?" });
  g.setEdge("have-some-software", "want-the-cloud", {style: happy_style,
            label: "I have one!",
            labelStyle: happy_label_style
  });
  g.setNode("get-hivewing", {style: happy_node_style, labelStyle: happy_node_label_style, label: "Get Hivewing.io!"});
  g.setEdge("want-the-cloud", 'get-hivewing', {style: happy_style,
            label: "I have one!",
            labelStyle: happy_label_style
  });

  //Noisy stuff
  g.setNode('noise1', {label:"noisy 1"});
  g.setEdge('noise1', 'want-the-cloud', {label: "really, you do?"});
  g.setNode('noise2', {label:"noisy 2"});
  g.setNode('noise3', {label:"noisy 3"});
  g.setEdge('noise3', 'have-some-software', {label: "really, you do?"});
  g.setEdge('noise5', 'have-some-software', {label: "really, you do?"});
  g.setNode('noise4', {label:"noisy 4"});
  g.setEdge('noise5', 'start', {label: "blerg"});
  g.setNode('noise5', {label:"noisy 5"});


  g.setEdge('noise2', 'want-the-cloud', {label: "can't figure you out"});


  var svg = d3.select("svg"),
      inner = svg.select("g");

  // Set up zoom support
  var zoom = d3.behavior.zoom().on("zoom", function() {
     inner.attr("transform", "translate(" + d3.event.translate + ")" +
                              "scale(" + d3.event.scale + ")");
  });
  svg.call(zoom);

  // Create the renderer
  var render = new dagreD3.render();

  // Run the renderer. This is what draws the final graph.
  render(inner, g);

  // Center the graph
  var initialScale = 0.75;
  zoom.translate([40, 40])
      .scale(initialScale)
        .event(svg);
        svg.attr('height', g.graph().height * initialScale + 40);
});
