# nolint start: line_length_linter.

options("width" = 60)

library("fs")

input <- path_expand("~/stats/benchmark-stats-2024-11-16-0-5120.txt")
raw_data <- read.csv(input, header = TRUE)

# Select interesting data subsets
recovery_data <- raw_data[raw_data$result == "recovery",]
error_data <- raw_data[raw_data$result == "error", ]
success_data <- raw_data[raw_data$result == "success", ]

drop <- c("source", "result")

recovery_fail_data <- recovery_data[recovery_data$errorSize >= recovery_data$size / 4, ]
recovery_ok_data <- recovery_data[recovery_data$errorSize < recovery_data$size / 4, ]

# Drop uninteresting columns
recovery <- recovery_data[, !(names(recovery_data) %in% drop)]
error <- error_data[, !(names(error_data) %in% drop)]
success <- success_data[, !(names(success_data) %in% drop)]
recovery_fail <- recovery_fail_data[, !(names(recovery_fail_data) %in% drop)]
recovery_ok <- recovery_ok_data[, !(names(recovery_ok_data) %in% drop)]

print("Total recovery stats")
summary(recovery)

print("Successful recovery stats (error size < 25% of file size)")
summary(recovery_ok)

print("Failed recovery stats (error size >= 25% of file size)")
summary(recovery_fail)

print("Parse error stats")
summary(error)

print("Successful parse stats")
summary(success)

# nolint end
