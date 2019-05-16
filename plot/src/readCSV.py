import numpy as np
import matplotlib.pyplot as plt
from scipy import stats
import glob


#
# read csvfile returns outdegree, node activity, mpc
# as matrix with the first row of values and second row of count
#
def readcsvfile(filename):
    # print(filename)
    with open(filename) as f:
        for i, line in enumerate(f):
            if i == 2:
                outDegreeCols = len(f.readline().split(','))
            if i == 4:
                naCols = len(f.readline().split(','))
            if i == 5:
                mpcCols = len(f.readline().split(','))

    f.close()
    # print(outDegreeCols, naCols, mpcCols)

    outdegree = np.genfromtxt(open(filename, "rb"), delimiter=",", skip_header=3, skip_footer=4, usecols=range(1, outDegreeCols))
    na = np.genfromtxt(open(filename, "rb"), delimiter=",", skip_header=5, skip_footer=2,
                              usecols=range(1, naCols))
    mpc = np.genfromtxt(open(filename, "rb"), delimiter=",", skip_header=7, skip_footer=0,
                              usecols=range(1, mpcCols))
    # print(outdegree)
    # print(na)
    # print(mpc)
    return outdegree, na, mpc


#
# take a matrix as input
# return the sample array
#
def getsample(dataMatrix):
    data = []
    value = dataMatrix[0, :]
    count = dataMatrix[1, :]
    for i, v in enumerate(value):
        for x in range(0, int(count[i])):
            data.append(v)
    return data


#
# take an array of filenames as input
# return the samples of outdegree, na, mpc
#
def getmetrics(filename):
    outdegree, na, mpc = readcsvfile(filename)
    outdegree_sample = getsample(outdegree)
    na_sample = getsample(na)
    mpc_sample = getsample(mpc)
    return outdegree_sample, na_sample, mpc_sample


#
# read number of files in the given path
#
def readmultiplefiles(dirName, numberOfFiles):
    list_of_files = glob.glob(dirName + '*.csv')  # create the list of file
    file_names = list_of_files[:numberOfFiles]
    # print(file_names)
    return file_names


def plotlines(x, y, ax):
    l1, = ax.plot(x, y)


def testgetsamplesfromfiles():
    files = readmultiplefiles('../viatraOutput/', 2)
    for file in files:
        getmetrics(file)

def probability(data):
    sum = np.sum(data)
    probabilityList = []
    for d in data:
        p = d/sum
        probabilityList.append(p)
    a = np.array(probabilityList)
    return a


def cumulativeProbability(p):
    cdf = np.cumsum(p)
    return cdf


def plot():
    fig, ax = plt.subplots()
    fig, ax1 = plt.subplots()
    fig, ax2 = plt.subplots()
    fig, ax3 = plt.subplots()
    fig, ax4 = plt.subplots()
    fig, ax5 = plt.subplots()
    list_of_files = readmultiplefiles('../viatraOutput/')
    for file_name in list_of_files:
        outdegree, na, mpc = readcsvfile(file_name)
        outV = outdegree[0, :]
        outC = outdegree[1, :]
        outP = probability(outC)
        outCumP = cumulativeProbability(outP)
        plotlines(outV, outP, ax)
        naV = na[0, :]
        naC = na[1, :]
        naP = probability(naC)
        naCumP = cumulativeProbability(naP)
        plotlines(naV, naP, ax1)
        mpcV = mpc[0, :]
        mpcC = mpc[1, :]
        mpcP = probability(mpcC)
        mpcCumP = cumulativeProbability(mpcP)
        plotlines(mpcV, mpcP, ax2)
        plotlines(outV, outCumP, ax3)
        plotlines(naV, naCumP, ax4)
        plotlines(mpcV, mpcCumP, ax5)
    ax.set_xlabel('ourdegree')
    ax.set_ylabel('pdf')
    ax.grid()

    ax1.set_xlabel('node activity')
    ax1.set_ylabel('pdf')
    ax1.grid()

    ax2.set_xlabel('multiplex participation coefficient')
    ax2.set_ylabel('pdf')
    ax2.grid()

    ax3.set_xlabel('ourdegree')
    ax3.set_ylabel('cdf')
    ax3.grid()

    ax4.set_xlabel('node activity')
    ax4.set_ylabel('cdf')
    ax4.grid()

    ax5.set_xlabel('multiplex participation coefficient')
    ax5.set_ylabel('cdf')
    ax5.grid()

    plt.show()


# plot()

