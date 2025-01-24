# Simple script to extract some statistics from ErrorRecoveryBenchmark statistics
import pandas as pd
import numpy as np
pd.set_option('display.max_columns', 1000, 'max_colwidth', 1000)

df = pd.DataFrame();

def load_stats(path):
    global df
    df = pd.concat([df, pd.read_csv("D:\\stats\\" + path)])

load_stats("benchmark-stats-2025-01-23-safe-memo-alts-rascal-0-5120.txt")
load_stats("benchmark-stats-2025-01-23-safe-memo-alts-salix-core-0-102400.txt")

verified = df[df["memoVerification"] == "memoSucceeded"]
verified['maxSharedNodes'] = pd.to_numeric(verified['maxSharedNodes'], errors='coerce')
verified['memoNodes'] = pd.to_numeric(verified['memoNodes'], errors='coerce')
verified['memoRatio'] = verified['memoNodes'] / verified['maxSharedNodes']

print(verified.describe());
print(verified)
