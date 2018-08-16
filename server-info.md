# CodeShovel Data

To run an instance of CodeShovel (**Note:** the command is also in the spreadsheet):

```sh
docker run --env REPO_NAME=XXX \
           --env START_COMMIT= \
           --env SEARCH_PATHS= \
           --volume "${HOME}/codeshovel-data/output:/var/opt/codeshovel/output" \
           --volume "${HOME}/codeshovel-data/repos:/repos:ro" \
           --name cs-XXX \
           --detach \
           codeshovel
```

## Logs

Execution output from the container should be recorded here in text files with the `container-name.date.log` naming convention:

```plain
logs/
  - cs-elasticsearch.20180805.log
  - cs-flink.20180805.log
```

To dump the log files from Docker: `docker logs container-name > container-name.date.log`

After dumping the log, remove the contianer with `docker rm container-name` to free resources.

To sync on the workstation, run

```sh
rsync -rtziOP --del nas.ncbradley.com:/var/opt/codeshovel/logs/ ~/codeshovel-data/logs
```

## Output

This directory should be bound to `/var/opt/codeshovel/output` in the container.

To sync on the workstation, run

```sh
rsync -rtziOP --del --force nas.ncbradley.com:/var/opt/codeshovel/output/ ~/codeshovel-data/output
```

## Repos

All the cloned repositories that are analyzed go here and should it should be bound to `/repos` in the container.

To sync on the workstation, run

```sh
rsync -rtziOP --del --force ~/codeshovel-data/repos/ nas.ncbradley.com:/var/opt/codeshovel/repos
```

## Ordering files

From the repository directory in `diff_semantic_gitlog`, run the following to get the 5 largest files sorted by size descending:

```sh
find . -type f -name "*.json" -exec ls -al {} \; | sort -k 5 -n | tail -n 10 | tac
```