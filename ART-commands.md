List of the legacy commands for ART
===================================

##Addpersonaldata
Usage: ```addpersonaldata \[w1 \[w2 \[w3 \[w4\]\]\]\]```
Description: percentage on how much to weight personal data (gender, age, job, zip code) in the prevision. The non set fields have the default value of 0, but if only w1 is 
set, its value is set for each field.


##Clustering
Usage: ```clustering command \[clustering-type \[n\]\]```
Description: start the clustering of the specified types, where n is the amount of clusters (n: 50). The command can be *calculate* or anything else, but only the former is going
to do anything: performing the specified type of clustering. 

The clustering-type can be any of the following:
 - cd (Community Detection)
 - kmeans
 - kmeanspersonalprediction
 - kmlocal
 - kmlocalpersonalprediction


##Forcek
Usage: ```forcek k```

Description: force the use of set number *k*


##Initcommunities
Usage: ```initcommunities clustering-type \[min\]```

Description: initialise the communities, where min represents the min group size (default: 1)


#Initmodeling
Usage: ```initmodeling modeling-type prediction-type \[coratings \[top \[trust \[whateveryoulike\]\]\]\]```
Description: start the group modeling and calculates the RMSE. Top is a server for predictions. (coratings: 0, top: 2, trust: -1.0). If there's the last parameter the personal predictions get enabled. *modeling-type* is an integer that indicates a choice for a specific
algorithm of group modeling:
 .0: AdditiveUtilitarian
 .1: BordaCount
 .2: NewApprovalVoting
 .3: MultiplicativeUtilitarian
 .4: NewLeastMisery
 .5: NewMostPleasure 
 .6: NewAvgWithoutMisery
 .7: NewApprovalVoting
 .8: NewApprovalVoting
 .9: NewApprovalVoting
 .10: NewAvgWithoutMisery
 .11: NewAvgWithoutMisery
 .12: NewAvgWithoutMisery


##Initnetwork
Usage: ```initnetwork cmd type```

Description: *cmd* can be 'calculate' or a random word. If a word different from
*calculate* is chosen, then it just set the name of the current network path in 
the environment. If *cmd* is *calculate* then it creates a file containing
the similarities between the test set and training set for each item.
The parameter *type* is used to choose which method to use to determine the 
similarities, and can be chosen among the following:

- adjustedcosine
- userbasedcosine
- itemsbasedcosine
- pearson
- newpearson


##Initsets
Usage: ```initsets command```

Description: call initDataset(command): if using *load* it just checks if
the partitions files exist and loads them, otherwise it creates them before
loading them.


##Loaddataset
Usage: ```loaddataset path```
Description: set the dataset_path to *path*


##Personalpredictions
Usage: ```personalpredictions cmd \[neighbours_amount\]```
Description: initialize the predictions, neighbours_amount refers the amount of 
neighbours (default: 100). The cmd must be *calculate* if you want it to do
anything.


##Workdir
Usage: ```workdir path```

Description: changes the base_dirname to path


##Other undocumented commands:

Usage: ```globaleffects command \[level\]```
Description: call initGlobalEffects(cmd, level); default level: 1

Usage: ```setnovelty n```
Description: set novelty to n

Usage: ```initmerging \[n\]```
Description: start the merge with value n (default: 5)

Usage: ```multiclustering type \[min \[max\]\]```
Description: start the clustering of the specified type, for clusters from min 
to max (min: 2, max: 50)

Usage: ```initstats \[whateveryouwant\]```
Description: run initstats!

Usage: ```precisionandrecall type-modeling \[group-prediction\]```
Description: calculates prediction and recall; group-prediction changes the 
group-prediction name

