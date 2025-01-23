# Simple script to extract some statistics from ErrorRecoveryBenchmark statistics
import pandas as pd
import numpy as np
pd.set_option('display.max_columns', 1000, 'max_colwidth', 1000)
data = pd.read_csv("D:/debug/memo/benchmark-stats-2025-01-21-safe-memo-alts-rascal-0-5120.txt")
#print(data.describe())

#print(data["memoNodes"].isnumeric().describe());
s = data['memoNodes']
#t = pd.to_numeric(s, errors='coerce')
data['maxSharedNodes'] = pd.to_numeric(data['maxSharedNodes'], errors='coerce')
data['memoNodes'] = pd.to_numeric(data['memoNodes'], errors='coerce')
data['memoRatio'] = data['memoNodes'] / data['maxSharedNodes']

filtered = data[data['memoRatio'] > 2]
sorted = filtered.sort_values(by=['memoNodes'])
print(sorted.describe())
print(sorted)

#r = data['memoNodes'].apply(lambda x: type(x) in [int, np.int64, float, np.float64])

#r = data[data.memoNodes.apply(lambda x: x.isnumeric())]
#print(r.describe())

#r  = data[pd.to_numeric(data['memoNodes'], errors='coerce').notnull()]
#print(r.describe())

#print(t)

#t = pd.to_numeric(data['memoNodes'], errors='coerce').notnull()
#print(t.describe())

#r = data["memoNodes"]/data["maxSharedNodes"]
#print("Memo ratios:")
#print(r.describe());

'''
print("Duration stats:")
print(data[data["result"] == "recovery"]["memoNodes"])
print("Disambiguation duration:")
print(data[data["result"] == "recovery"]["disambiguationDuration"].describe())
'''
