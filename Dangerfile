# Warn when there is a big PR
if git.lines_of_code > 500 
	warn("Please consider breaking up this Pull Request.")
end

# Warn when no labels have been added
if github.pr_labels.empty?
	warn("Please add labels to this PR.")
end

# Check PR description size
if github.pr_body.length == 0
  fail("Please provide a summary in the Pull Request description.")
end