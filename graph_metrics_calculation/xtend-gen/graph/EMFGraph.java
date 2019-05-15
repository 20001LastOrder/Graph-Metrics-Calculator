package graph;

import graph.GraphStatistic;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class EMFGraph {
  private final GraphStatistic statistic = new GraphStatistic();
  
  public void init(final EObject root) {
    this.init(root.eAllContents());
  }
  
  public void init(final Iterator<EObject> container) {
    final List<EObject> objects = IteratorExtensions.<EObject>toList(container);
    final Consumer<EObject> _function = (EObject it) -> {
      this.statistic.addNode(it);
    };
    objects.forEach(_function);
    final Consumer<EObject> _function_1 = (EObject source) -> {
      final Consumer<EReference> _function_2 = (EReference r) -> {
        final Consumer<EObject> _function_3 = (EObject target) -> {
          this.addEdge(source, target, r);
        };
        this.getNeighbours(source, r).forEach(_function_3);
      };
      source.eClass().getEReferences().forEach(_function_2);
    };
    objects.forEach(_function_1);
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
}
