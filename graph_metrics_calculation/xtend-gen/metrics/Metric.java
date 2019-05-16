package metrics;

import graph.GraphStatistic;

@SuppressWarnings("all")
public abstract class Metric {
  public abstract String[][] evaluate(final GraphStatistic g);
}
