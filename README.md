# The Gradle Odyssey

A conference talk (and its companion code) about reusing Gradle build configuration — told as a journey, Homer-style.

## The Journey

| Stage | Gradle technique | Theme |
|---|---|---|
| Ithaca | Single-module build | Home base — where every build starts |
| Setting sail | Multi-module build | A fleet of modules, one voyage |
| The Cyclops's cave | `buildSrc` | Powerful, but trapped on one island (no cross-repo reuse) |
| The Sirens | Version catalogs & BOMs | The tempting shortcut of centralizing versions |
| Ithaca, regained | Published, shared plugin | Home again — transformed by everything learned along the way |

## Structure

- `slides/` — [Slidev](https://sli.dev/) markdown source for the talk
- Module folders (one per stage) — runnable Gradle examples demonstrating each reuse technique, its trade-offs, and when to reach for it

## Status

Work in progress — scaffolding the demo modules and slide deck.