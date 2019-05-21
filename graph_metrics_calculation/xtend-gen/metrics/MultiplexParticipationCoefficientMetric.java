package metrics;

import graph.GraphStatistic;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import metrics.Metric;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class MultiplexParticipationCoefficientMetric extends Metric {
  private static final String countName = "MPCCount";
  
  private static final String valueName = "MPCValue";
  
  @Override
  public String[][] evaluate(final GraphStatistic g) {
    final DecimalFormat formatter = new DecimalFormat("#0.00000");
    final int typeCounts = g.getAllTypes().size();
    final HashMap<String, Integer> map = new HashMap<String, Integer>();
    final Consumer<EObject> _function = (EObject n) -> {
      int _outDegree = g.outDegree(n);
      int _inDegree = g.inDegree(n);
      final int edgeCounts = (_outDegree + _inDegree);
      double coef = 0.0;
      List<String> _allTypes = g.getAllTypes();
      for (final String type : _allTypes) {
        {
          int _dimentionalDegree = g.dimentionalDegree(n, type);
          final double degree = ((double) _dimentionalDegree);
          double _coef = coef;
          double _pow = Math.pow((degree / edgeCounts), 2);
          coef = (_coef + _pow);
        }
      }
      coef = (1 - coef);
      coef = ((coef * typeCounts) / (typeCounts - 1));
      if ((typeCounts == 1)) {
        InputOutput.<String>println("bad");
      }
      boolean _isNaN = Double.isNaN(coef);
      if (_isNaN) {
        coef = 0;
      }
      final String value = formatter.format(coef);
      boolean _containsKey = map.containsKey(value);
      boolean _not = (!_containsKey);
      if (_not) {
        map.put(value, Integer.valueOf(1));
      } else {
        Integer _get = map.get(value);
        int _plus = ((_get).intValue() + 1);
        map.put(value, Integer.valueOf(_plus));
      }
    };
    g.getAllNodes().forEach(_function);
    int _size = map.size();
    int _plus = (_size + 1);
    final String[][] datas = new String[2][_plus];
    datas[0][0] = MultiplexParticipationCoefficientMetric.valueName;
    datas[1][0] = MultiplexParticipationCoefficientMetric.countName;
    int count = 1;
    final Function1<Map.Entry<String, Integer>, String> _function_1 = (Map.Entry<String, Integer> it) -> {
      return it.getKey();
    };
    List<Map.Entry<String, Integer>> _sortBy = IterableExtensions.<Map.Entry<String, Integer>, String>sortBy(map.entrySet(), _function_1);
    for (final Map.Entry<String, Integer> entry : _sortBy) {
      {
        String _key = entry.getKey();
        String _plus_1 = (_key + "");
        datas[0][count] = _plus_1;
        Integer _value = entry.getValue();
        String _plus_2 = (_value + "");
        datas[1][count] = _plus_2;
        count++;
      }
    }
    return datas;
  }
}
