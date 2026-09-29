# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commit messages

Do not add a `Co-Authored-By: Claude` trailer to commits in this repository.

## Superpowers artifact locations

Use `.claude/` for all superpowers-generated artifacts in this repository, overriding any default `docs/superpowers/` location:

- Design specs → `.claude/specs/YYYY-MM-DD-<topic>-design.md`
- Implementation plans → `.claude/plans/YYYY-MM-DD-<feature-name>.md`

Both directories are gitignored — these artifacts are local working documents, not committed to the repo.

## Stage slide template

Each of the 5 Odyssey stages (`slides/pages/NN-<stage>.md`) follows a fixed 5-slide structure, in order:

1. **Introduction** — heading with `<StageIcon>`, `<WaveDivider />`, one-line theme description.
2. **Schematic view** — diagram of the proposed Gradle solution.
3. **Code demo** — the runnable example for this stage.
4. **Pros and cons** — trade-offs of the technique.
5. **Conclusion** — wrap-up / transition to the next stage.

Sections 1→2 and 2→3 use `transition: slide-down` (diving into detail); sections 3→4 and 4→5 use `transition: slide-up` (pulling back out to the big picture). Transitioning between stages (one stage's Conclusion to the next stage's Introduction) uses `transition: slide-left`, set on the `src:` import block in `slides/slides.md`.