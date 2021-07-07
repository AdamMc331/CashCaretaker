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

# Fail when the PR title does not match our standard
# Only perform this check if the head branch is not development or production.
if !["development", "production"].include? github.branch_for_head
  titleRegex = "/[a-z]{2,3}\/([A-Z]){2,6}-[0-9]+\/.*/"

  if github.pr_title !~ titleRegex
    fail("Please follow the PR title standard {INITIALS}/{TICKET-NUMBER}.")
  end
end