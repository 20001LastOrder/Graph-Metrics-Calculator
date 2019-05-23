package graph;

import com.google.common.collect.Iterables;
import graph.Graph;
import graph.GraphStatistic;
import hu.bme.mit.inf.dslreasoner.logic.model.logiclanguage.DefinedElement;
import hu.bme.mit.inf.dslreasoner.logic.model.logiclanguage.Relation;
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.BinaryElementRelationLink;
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.PartialInterpretation;
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.PartialRelationInterpretation;
import hu.bme.mit.inf.dslreasoner.viatrasolver.partialinterpretationlanguage.partialinterpretation.impl.BooleanElementImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import metrics.Metric;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class PartialInterpretationGraph extends Graph {
  private String lastStateId = "";
  
  public PartialInterpretationGraph(final PartialInterpretation partial, final List<Metric> metrics, final String name) {
    final Consumer<Relation> _function = (Relation it) -> {
      this.statistic.addType(it.getName());
    };
    partial.getProblem().getRelations().forEach(_function);
    final Iterable<DefinedElement> elements = this.getElements(partial);
    for (final DefinedElement element : elements) {
      this.statistic.addNode(element);
    }
    EList<PartialRelationInterpretation> _partialrelationinterpretation = partial.getPartialrelationinterpretation();
    for (final PartialRelationInterpretation relationInterpretation : _partialrelationinterpretation) {
      {
        final String type = relationInterpretation.getInterpretationOf().getName();
        Iterable<BinaryElementRelationLink> _filter = Iterables.<BinaryElementRelationLink>filter(relationInterpretation.getRelationlinks(), BinaryElementRelationLink.class);
        for (final BinaryElementRelationLink edge : _filter) {
          this.statistic.addEdge(edge.getParam1(), edge.getParam2(), type);
        }
      }
    }
    this.name = name;
    this.metrics = metrics;
  }
  
  /**
   * Set basic information for the output
   */
  @Override
  public void setBasicInformation(final ArrayList<ArrayList<String>> output) {
    final ArrayList<String> metaInfo = new ArrayList<String>();
    metaInfo.add(Graph.META_MODEL_HEADER);
    metaInfo.add(this.metaModel);
    final ArrayList<String> edgeInfo = new ArrayList<String>();
    edgeInfo.add(Graph.NUM_EDGE_TYPE_HEADER);
    int _size = this.statistic.getAllTypes().size();
    String _plus = (Integer.valueOf(_size) + "");
    edgeInfo.add(_plus);
    final ArrayList<String> nodeInfo = new ArrayList<String>();
    nodeInfo.add(Graph.NUM_NODE_HEADER);
    int _size_1 = this.statistic.getAllNodes().size();
    String _plus_1 = (Integer.valueOf(_size_1) + "");
    nodeInfo.add(_plus_1);
    final ArrayList<String> stateInfo = new ArrayList<String>();
    stateInfo.add(Graph.STATE_ID_HEADER);
    stateInfo.add(this.name);
    output.add(metaInfo);
    output.add(edgeInfo);
    output.add(nodeInfo);
    output.add(stateInfo);
  }
  
  private Iterable<DefinedElement> getElements(final PartialInterpretation partial) {
    final Function1<DefinedElement, Boolean> _function = (DefinedElement it) -> {
      return Boolean.valueOf((!(it instanceof BooleanElementImpl)));
    };
    Iterable<DefinedElement> _filter = IterableExtensions.<DefinedElement>filter(partial.getNewElements(), _function);
    EList<DefinedElement> _elements = partial.getProblem().getElements();
    return Iterables.<DefinedElement>concat(_filter, _elements);
  }
  
  @Override
  public GraphStatistic getStatistic() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  public String getName() {
    return this.name;
  }
}
