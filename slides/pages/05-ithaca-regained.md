---
transition: slide-up
---

# <StageIcon name="laurel" :size="40" /> Ithaca, Regained

<WaveDivider />

Published, shared plugin — home again, transformed

`buildSrc` becomes a published plugin — reusable far beyond this repo.

<!--
The final transformation: buildSrc's convention plugins are extracted into their own
standalone Gradle plugin project, build-logic-plugin, published to mavenLocal(). Instead of
copying build logic into every new repo, heroes-service and monsters-service simply apply
it via plugins { id(...) } — versioned through the same libs.versions.toml catalog.

This is the payoff for the whole journey: what started as a single module's build.gradle.kts
is now build logic any project can adopt, with no copying and no drift.
-->

---
transition: slide-up
---

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {theme: 'dark', scale: 0.8}
flowchart LR
    heroes["heroes-service"] --> apispec["api-spec"]
    monsters["monsters-service"] --> apispec
    plugin["build-logic-plugin (published)"]:::external -.-> heroes
    plugin -.-> monsters
    plugin -.-> apispec
    catalog["libs.versions.toml"] -.->|versions| heroes
    catalog -.->|versions| monsters
    catalog -.->|versions| apispec
    catalog -.->|versions| plugin

    classDef external fill:#7a1f2b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
```

</div>

---
transition: slide-down
---

## Code Demo

<!--
TODO: content
-->

---
transition: slide-down
---

## Pros & Cons

<div class="grid grid-cols-2 gap-x-8 mt-4">
  <div>
    <h3 style="color: var(--odyssey-gold)">Pros</h3>
    <ul>
      <li>Build logic published once, consumed by any project via <code>plugins { id(...) }</code></li>
      <li>True reuse — no copy-pasting <code>buildSrc</code> or the catalog into new repos</li>
      <li>Central place to version and release build-logic changes</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-wine)">Cons</h3>
    <ul>
      <li>Adds publishing and versioning ceremony for the plugin itself</li>
      <li>Requires a real (or local) plugin repository and release discipline</li>
      <li>Shared build-logic changes now need their own release before consumers pick them up</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

## Conclusion

<div class="flex justify-center items-center h-full text-center text-xl">

Home again, transformed — build logic any project can adopt, with no copying, no drift.

</div>

<!--
Home again, transformed: what started as a single module is now build logic any project
can adopt, published and versioned like any other dependency — no copying, no drift, no cave.
The voyage that began with one simple build ends with one that scales to as many as you need.
-->
