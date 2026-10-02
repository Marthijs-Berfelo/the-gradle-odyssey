# Odyssey demo — stage worktrees

Each of the 5 talk stages is a git tag; this folder's scripts turn those tags into
separate, independent working directories (git worktrees) so a live-coding mistake in
one stage never touches another.

## One-time setup

Add this to your shell rc (`~/.zshrc` or `~/.bashrc`):

```bash
source /path/to/the-gradle-odyssey/demo/scripts/_open-stage-completions.bash

stage() {
  cd "$(/path/to/the-gradle-odyssey/demo/scripts/open-stage.sh "$1")"
}
complete -F _open_stage_completions stage
```

Then: `demo/scripts/checkout-stages.sh` (rerun any time to pick up new/updated tags).

## During the talk

```bash
stage 01-ithaca            # <TAB> completes stage names
./gradlew bootTestRun
```

If a live edit breaks something mid-stage, just `stage <next-stage-name>` — the next
stage's worktree is untouched.
