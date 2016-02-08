Possible improvements
=====================

- Dataset cross-validation:
Dataset uses a stratified k-fold cross-validation, this reduced the 
variance of each fold. Evaluate the use of a value of 10 for the k,
as a 10-fold cross-validation.

- Delete possible outlier values before k-fold
