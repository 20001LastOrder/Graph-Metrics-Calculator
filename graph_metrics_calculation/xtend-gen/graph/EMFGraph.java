package graph;

import graph.GraphStatistic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import metrics.Metric;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class EMFGraph {
  private final GraphStatistic statistic = new GraphStatistic();
  
  private List<Metric> metrics;
  
  private String name;
  
  private String metaModel;
  
  private static final String META_MODEL_HEADER = "Meta Mode";
  
  private static final String NUM_NODE_HEADER = "Number Of Nodes";
  
  private static final String NUM_EDGE_TYPE_HEADER = "Number of Edge types";
  
  public void init(final EObject root, final List<Metric> metrics, final String name) {
    final List<EObject> otherContents = IteratorExtensions.<EObject>toList(root.eAllContents());
    this.init(otherContents, metrics, name);
  }
  
  public void init(final List<EObject> objects, final List<Metric> metrics, final String name) {
    final Consumer<EObject> _function = (EObject it) -> {
      this.statistic.addNode(it);
    };
    objects.forEach(_function);
    final Consumer<EObject> _function_1 = (EObject source) -> {
      final Consumer<EReference> _function_2 = (EReference r) -> {
        boolean _isMany = r.isMany();
        if (_isMany) {
          final Consumer<EObject> _function_3 = (EObject target) -> {
            this.addEdge(source, target, r);
          };
          this.getNeighbours(source, r).forEach(_function_3);
        } else {
          Object _eGet = source.eGet(r);
          final EObject target = ((EObject) _eGet);
          this.addEdge(source, target, r);
        }
      };
      source.eClass().getEAllReferences().forEach(_function_2);
    };
    objects.forEach(_function_1);
    this.metrics = metrics;
    this.name = name;
  }
  
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
  
  private boolean setBasicInformation(final ArrayList<ArrayList<String>> output) {
    boolean _xblockexpression = false;
    {
      final ArrayList<String> metaInfo = new ArrayList<String>();
      metaInfo.add(EMFGraph.META_MODEL_HEADER);
      metaInfo.add(this.metaModel);
      final ArrayList<String> edgeInfo = new ArrayList<String>();
      edgeInfo.add(EMFGraph.NUM_EDGE_TYPE_HEADER);
      int _size = this.statistic.getAllTypes().size();
      String _plus = (Integer.valueOf(_size) + "");
      edgeInfo.add(_plus);
      final ArrayList<String> nodeInfo = new ArrayList<String>();
      nodeInfo.add(EMFGraph.NUM_NODE_HEADER);
      int _size_1 = this.statistic.getAllNodes().size();
      String _plus_1 = (Integer.valueOf(_size_1) + "");
      nodeInfo.add(_plus_1);
      output.add(metaInfo);
      output.add(edgeInfo);
      _xblockexpression = output.add(nodeInfo);
    }
    return _xblockexpression;
  }
  
  public EList<EObject> getNeighbours(final EObject o, final EReference r) {
    Object _eGet = o.eGet(r, true);
    return ((EList<EObject>) _eGet);
  }
  
  public void addEdge(final EObject source, final EObject target, final EReference r) {
    if (((target != null) && (r != null))) {
      this.statistic.addEdge(source, target, r.getName());
    }
  }
  
  public GraphStatistic getStatistic() {
    return this.statistic;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setMetaModel(final String model) {
    this.metaModel = model;
  }
  
  public String getMetaModel() {
    return this.metaModel;
  }
}
