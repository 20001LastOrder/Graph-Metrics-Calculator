package graph;

import graph.GraphStatistic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import metrics.Metric;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public abstract class Graph {
  protected static final String META_MODEL_HEADER = "Meta Mode";
  
  protected static final String NUM_NODE_HEADER = "Number Of Nodes";
  
  protected static final String NUM_EDGE_TYPE_HEADER = "Number of Edge types";
  
  protected static final String STATE_ID_HEADER = "State Id";
  
  protected final GraphStatistic statistic = new GraphStatistic();
  
  protected List<Metric> metrics;
  
  protected String name = "";
  
  protected String metaModel = "";
  
  /**
   * evaluate all metrics for this model
   * return the result as a two dimentional list
   */
  public ArrayList<ArrayList<String>> evaluateAllMetrics() {
    final ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    this.setBasicInformation(result);
    for (final Metric metric : this.metrics) {
      {
        final String[][] datas = metric.evaluate(this.statistic);
        for (final String[] row : datas) {
          ArrayList<String> _arrayList = new ArrayList<String>((Collection<? extends String>)Conversions.doWrapArray(row));
          result.add(_arrayList);
        }
      }
    }
    return result;
  }
  
  public abstract void setBasicInformation(final ArrayList<ArrayList<String>> result);
  
  public abstract GraphStatistic getStatistic();
  
  public abstract String getName();
}
