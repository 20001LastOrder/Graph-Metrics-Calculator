package graph;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class GraphStatistic {
  private final HashMap<String, Multimap<EObject, EObject>> incomingEdges = new HashMap<String, Multimap<EObject, EObject>>();
  
  private final HashMap<String, Multimap<EObject, EObject>> outcomingEdges = new HashMap<String, Multimap<EObject, EObject>>();
  
  private final HashSet<String> edgeTypes = new HashSet<String>();
  
  private final HashSet<EObject> nodes = new HashSet<EObject>();
  
  /**
   * Add an edge type to to the graph
   * @param type: type to add
   */
  private void addType(final String type) {
    this.edgeTypes.add(type);
    this.incomingEdges.put(type, ArrayListMultimap.<EObject, EObject>create());
    this.outcomingEdges.put(type, ArrayListMultimap.<EObject, EObject>create());
  }
  
  /**
   * Add a node to he graph
   * @param node: node to add
   */
  public void addNode(final EObject n) {
    boolean _contains = this.nodes.contains(n);
    if (_contains) {
      return;
    }
    this.nodes.add(n);
  }
  
  /**
   * Add an edge to the graph
   * @param source: source node
   * @param target: target node
   * @param type: type of the reference
   */
  public void addEdge(final EObject source, final EObject target, final String type) {
    boolean _contains = this.edgeTypes.contains(type);
    boolean _not = (!_contains);
    if (_not) {
      this.addType(type);
    }
    this.outcomingEdges.get(type).put(source, target);
    this.incomingEdges.get(type).put(target, source);
  }
  
  /**
   * calculate the out degree for an object
   */
  public int outDegree(final EObject o) {
    int count = 0;
    for (final String type : this.edgeTypes) {
      int _count = count;
      int _size = this.outcomingEdges.get(type).get(o).size();
      count = (_count + _size);
    }
    return count;
  }
  
  /**
   * calculate the in degree of an object
   */
  public int inDegree(final EObject o) {
    int count = 0;
    for (final String type : this.edgeTypes) {
      int _count = count;
      int _size = this.incomingEdges.get(type).get(o).size();
      count = (_count + _size);
    }
    return count;
  }
  
  /**
   * calculate the dimentional degree of a node
   */
  public int dimentionalDegree(final EObject o, final String type) {
    int _size = this.incomingEdges.get(type).get(o).size();
    int _size_1 = this.outcomingEdges.get(type).get(o).size();
    return (_size + _size_1);
  }
  
  /**
   * calculate the number of edge types for a given degree.
   */
  public int numOfEdgeTypes(final EObject o) {
    int count = 0;
    for (final String type : this.edgeTypes) {
      int _dimentionalDegree = this.dimentionalDegree(o, type);
      boolean _greaterThan = (_dimentionalDegree > 0);
      if (_greaterThan) {
        count++;
      }
    }
    return count;
  }
  
  public List<String> getAllTypes() {
    return IterableExtensions.<String>toList(this.edgeTypes);
  }
  
  public List<EObject> getAllNodes() {
    return IterableExtensions.<EObject>toList(this.nodes);
  }
}
