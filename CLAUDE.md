# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commit messages

Do not add a `Co-Authored-By: Claude` trailer to commits in this repository.

## Superpowers artifact locations

Use `.claude/` for all superpowers-generated artifacts in this repository, overriding any default `docs/superpowers/` location:

- Design specs → `.claude/specs/YYYY-MM-DD-<topic>-design.md`
- Implementation plans → `.claude/plans/YYYY-MM-DD-<feature-name>.md`

Both directories are gitignored — these artifacts are local working documents, not committed to the repo.