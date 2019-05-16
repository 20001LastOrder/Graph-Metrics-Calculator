import readCSV as reader
import glob
from sklearn.manifold import MDS
import matplotlib.pyplot as plt
from scipy import stats
import numpy as np

def calculateKSMatrix(gen_dist, hu_dist):
    dist = gen_dist + hu_dist
    matrix = np.empty((len(dist),len(dist)))

    for i in range(len(dist)):
        matrix[i,i] = 0
        for j in range(i+1, len(dist)):
            value, p = stats.ks_2samp(dist[i], dist[j])
            matrix[i, j] = value
            matrix[j, i] = value
    return matrix

def calculateMDS(dissimilarities):
    embedding = MDS(n_components=2, dissimilarity='precomputed')
    trans = embedding.fit_transform(X=dissimilarities)
    return trans

def plot(gen_name, hu_name, coords, index = 0, title=''):
    half_length = int(coords.shape[0] / 2)
    x = (coords[:half_length,0]).tolist()
    y = (coords[:half_length,1]).tolist()

    x2 = (coords[half_length:,0]).tolist()
    y2 = (coords[half_length:,1]).tolist()
    g = plt.figure(index)
    plt.title(title)
    plt.plot(x, y, 'yo', label = gen_name)
    plt.plot(x2, y2, 'ro', label = hu_name)
    plt.legend(loc='upper right')
    plt.savefig(fname = title+'.png', dpi=150)
    g.show()


#read models
generatedModels = reader.readmultiplefiles('../viatraOutput/', 500)

gen_out_ds = []
gen_nas = []
gen_mpcs = []

for i in range(len(generatedModels)):
    gen_out_d, gen_na, gen_mpc = reader.getmetrics(generatedModels[i])
    gen_out_ds.append(gen_out_d)
    gen_nas.append(gen_na)
    gen_mpcs.append(gen_mpc)

humanModels = reader.readmultiplefiles('../output/', 500)
hu_out_ds = []
hu_nas = []
hu_mpcs = []
for i in range(len(humanModels)):
    hu_out_d, hu_na, hu_mpc = reader.getmetrics(humanModels[i])
    hu_out_ds.append(hu_out_d)
    hu_nas.append(hu_na)
    hu_mpcs.append(hu_mpc)


print('Calculating for out degree\n')
out_d_coords = calculateMDS(calculateKSMatrix(gen_out_ds, hu_out_ds))
plot('Generated Out Degree', 'Human Out Degree', out_d_coords,0, 'Out Degree')

print('Calculating for node activity\n')
out_d_coords = calculateMDS(calculateKSMatrix(gen_nas, hu_nas))
plot('Generated Node Activity', 'Human Node Activity', out_d_coords,1, 'Node Activity')

print('Calculating for MPC')
out_d_coords = calculateMDS(calculateKSMatrix(gen_mpcs, hu_mpcs))
plot('Generated MPC', 'Human MPC', out_d_coords,2, 'MPC')
input()