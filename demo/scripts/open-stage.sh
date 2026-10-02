#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(git rev-parse --show-toplevel)"
WORKTREES_DIR="$REPO_ROOT/demo/.worktrees"

usage() {
  echo "Usage: open-stage.sh <stage-name>"
  echo "Available stages:"
  for d in "$WORKTREES_DIR"/*/; do
    [ -d "$d" ] && echo "  $(basename "$d")"
  done
}

if [ "${1:-}" = "" ]; then
  usage
  exit 1
fi

target="$WORKTREES_DIR/$1"

if [ ! -d "$target" ]; then
  echo "error: no worktree at $target" >&2
  usage
  exit 1
fi

echo "$target"
