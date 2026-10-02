#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(git rev-parse --show-toplevel)"
WORKTREES_DIR="$REPO_ROOT/demo/.worktrees"

# Parallel arrays instead of an associative array: macOS ships bash 3.2,
# which has no `declare -A` support.
STAGE_DIRS=(01-ithaca 02-setting-sail 03-cyclops-cave 04-sirens 05-ithaca-regained)
STAGE_TAGS=(stage-1-ithaca stage-2-setting-sail stage-3-cyclops-cave stage-4-sirens stage-5-ithaca-regained)

mkdir -p "$WORKTREES_DIR"

for i in "${!STAGE_DIRS[@]}"; do
  dir="${STAGE_DIRS[$i]}"
  tag="${STAGE_TAGS[$i]}"
  target="$WORKTREES_DIR/$dir"

  if ! git -C "$REPO_ROOT" rev-parse "$tag" >/dev/null 2>&1; then
    echo "skip: tag '$tag' does not exist yet"
    continue
  fi

  if [ -d "$target" ]; then
    echo "update: $dir (already checked out)"
    continue
  fi

  echo "create: $dir <- $tag"
  git -C "$REPO_ROOT" worktree add "$target" "$tag"
done
