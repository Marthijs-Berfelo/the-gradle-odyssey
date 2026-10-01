---
transition: slide-up
---

# <StageIcon name="column" :size="40" /> Ithaca

<WaveDivider />

Single-module build — home base, where every project starts

<br/>

One module, **`odyssey-app`**, holds both of our domain entities:

- **Hero** — `id`, `name`, `epithet`, `strength`
- **Monster** — `id`, `name`, `domain`, `danger`

One build, one deployable.

<!--
Before the war, Odysseus is content — king of Ithaca, one household, one domain. But he swore
an oath to Tyndareus: if ever Helen's marriage were threatened, every suitor would rally to
defend it. When Paris takes her to Troy, Agamemnon calls in that oath, and Odysseus has no
choice but to sail. One ship can't fight a ten-year siege alone — the campaign demands separate
forces: infantry, cavalry, the fleet, each moving independently. That's the mobilization order
our build receives too: Hero and Monster have outgrown the household. They need to become
separate forces capable of shipping on their own.

Every odyssey begins at home. Our journey starts with odyssey-app — one Gradle module,
one Spring Boot application, housing both of our domain entities side by side.

A single build.gradle.kts wires up Spring Boot, Kotlin, R2DBC, and Flyway for the whole app.
No modules to split, no boundaries to cross — just one build, one deployable, one place to look.
-->

---
transition: slide-up
---

<StageFooter icon="column" name="Ithaca" />

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {scale: 1.2}
%%{init: {'theme': 'base', 'themeVariables': {
  'primaryColor': '#3d2f14',
  'primaryTextColor': '#e8dcc4',
  'primaryBorderColor': '#d4af37',
  'lineColor': '#d4af37',
  'edgeLabelBackground': '#0d1b2a',
  'fontFamily': 'Georgia, serif'
}}}%%
flowchart TB
    subgraph app["odyssey-app — one repo"]
        hero["hero package"]
        monster["monster package"]
        build["build.gradle.kts"]:::config
    end
    build -.->|configures| hero
    build -.->|configures| monster
    style app fill:#0d1b2a,stroke:#d4af37,stroke-width:2px,stroke-dasharray:4 4
    classDef config fill:#7a1f2b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
```

</div>

---
transition: slide-down
---

<StageFooter icon="column" name="Ithaca" />

## Code Demo

<!--
TODO: content
-->

---
transition: slide-down
---

<StageFooter icon="column" name="Ithaca" />

## Pros & Cons

<br/>

<div class="grid grid-cols-2 gap-x-8 mt-4">
  <div>
    <h3 style="color: var(--odyssey-gold); font-weight: 700">Pros</h3>
    <ul>
      <li>Zero setup — one <code>build.gradle.kts</code>, one command to build and run</li>
      <li>Single dependency graph, trivial to reason about</li>
      <li>No build-logic duplication, because there's only one build</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-rose); font-weight: 700">Cons</h3>
    <ul>
      <li>Heroes and Monsters can't deploy independently</li>
      <li>A change to either domain forces rebuilding and redeploying both</li>
      <li>Team ownership boundaries blur inside one module</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

<StageFooter icon="column" name="Ithaca" />

## Conclusion

<div class="absolute inset-0 flex items-center justify-center text-center text-xl px-20">

One module, one build — but Heroes and Monsters need to ship on separate schedules.

</div>

<!--
The simplest way to start a journey: one module, one build, nothing to configure.
But Heroes and Monsters have outgrown the same ship — they need to ship on separate
schedules, and a single module can't give them that. Time to set sail.
-->
