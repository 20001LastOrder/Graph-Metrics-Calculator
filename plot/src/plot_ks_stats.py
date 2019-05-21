import readCSV as reader
import glob
import random 
from sklearn.manifold import MDS
import matplotlib.pyplot as plt
from scipy import stats
import numpy as np

class GraphType:
    
    # init with path contrain files and number of files to read reader is imported from (readCSV)
    def __init__(self, path, number, name):
        self.out_ds = []
        self.nas = []
        self.mpcs = []
        self.name = name
        models = reader.readmultiplefiles(path, number)
        for i in range(len(models)):
            out_d, na, mpc = reader.getmetrics(models[i])
            self.out_ds.append(out_d)
            self.nas.append(na)
            self.mpcs.append(mpc)

def calculateKSMatrix(dists):
    dist = []

    for i in range(len(dists)):
        dist = dist + dists[i]
    matrix = np.empty((len(dist),len(dist)))

    for i in range(len(dist)):
        matrix[i,i] = 0
        for j in range(i+1, len(dist)):
            value, p = stats.ks_2samp(dist[i], dist[j])
            matrix[i, j] = value
            matrix[j, i] = value
            value, p = stats.ks_2samp(dist[j], dist[i])
    return matrix


def calculateMDS(dissimilarities):
    embedding = MDS(n_components=2, dissimilarity='precomputed')
    trans = embedding.fit_transform(X=dissimilarities)
    return trans

def plot(graphTypes, coords, title='', savePath = ''):
    half_length = int(coords.shape[0] / len(graphTypes))
    color = ['blue', 'red', 'green']
    graph = plt.figure(0)
    plt.title(title)
    for i in range(len(graphTypes)):
        x = (coords[(i*half_length):((i+1)*half_length), 0].tolist())
        y = (coords[(i*half_length):((i+1)*half_length), 1].tolist())
        plt.plot(x, y, color=color[i], marker='o', label = graphTypes[i].name, linestyle='', alpha=0.7)
    plt.legend(loc='upper right')
    plt.savefig(fname = savePath, dpi=150)
    #graph.show()

def mkdir_p(mypath):
    '''Creates a directory. equivalent to using mkdir -p on the command line'''

    from errno import EEXIST
    from os import makedirs,path

    try:
        makedirs(mypath)
    except OSError as exc: # Python >2.5
        if exc.errno == EEXIST and path.isdir(mypath):
            pass
        else: raise

def metricStat(graphTypes, metricName, metric):
    metrics = []
    foldName = '../output/'
    for graph in graphTypes:
        metrics.append(metric(graph))
        foldName = foldName + graph.name + '-'
    print('calculate' + metricName +' for ' + foldName)
    mkdir_p(foldName)
    out_d_coords = calculateMDS(calculateKSMatrix(metrics))
    plot(graphTypes, out_d_coords, metricName, foldName + '/'+ metricName+'.png')

def nodeActivity(graphType):
    return graphType.nas

def outDegree(graphType):
    return graphType.out_ds

def mpc(graphType):
    return graphType.mpc


# read models
human = GraphType('../statistics/humanOutput/', 500, 'Human')
viatra30 = GraphType('../statistics/viatraOutput30/', 500, 'Viatra (30 nodes)')
viatra100 = GraphType('../statistics/viatraOutput100/', 500, 'Viatra (100 nodes)')
random = GraphType('../statistics/randomOutput/', 500, 'Random')
alloy = GraphType('../statistics/alloyOutput/', 500, 'Alloy (30 nodes)')

#calculate metrics
metricStat([human, viatra30, random], 'Node Activity', nodeActivity)
metricStat([human, viatra30, random], 'Out Degree', outDegree)
metricStat([human, viatra30, random], 'MPC', mpc)