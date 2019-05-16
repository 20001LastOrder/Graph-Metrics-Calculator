package metrics

import graph.GraphStatistic

abstract class Metric {
	abstract def String[][] evaluate(GraphStatistic g);
}