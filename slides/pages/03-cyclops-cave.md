---
transition: slide-up
---

# <StageIcon name="eye" :size="40" /> The Cyclops's Cave

<WaveDivider />

`buildSrc` — powerful, but trapped on one island

<br/>

One `buildSrc` convention plugin replaces four copies of build boilerplate.

<!--
The four modules from Setting Sail each hand-rolled their own Spring Boot, Kotlin, and
OpenAPI-codegen setup. buildSrc lets us pull that duplicated logic into one convention
plugin, applied by heroes-service, monsters-service, encounters-service, and api-spec
alike — a single source of truth for this repo's build logic.

The catch: buildSrc only exists inside this repo. Like the Cyclops's cave, it's powerful
but sealed off — nothing inside it can be reused by any other project without copying the
whole thing over again.

Odysseus doesn't just sit in the cave and despair — he blinds Polyphemus and escapes by
lashing himself and his men to the undersides of the cyclops's own sheep, slipping past a
blinded guard who's only checking for men standing upright. It's the same cleverness some
teams reach for with buildSrc: pull it in as a git submodule, or copy it out again in CI.
Clever, and it can work for a while — but it's a trick, not a strategy. The cave is still a
cave. The moment you need this logic in a third repo, you're back to copying sheep.
-->

---
transition: slide-up
---

<StageFooter icon="eye" name="The Cyclops's Cave" />

## Schematic View

<div class="flex justify-center items-center h-full">

```mermaid {scale: 0.85}
%%{init: {'theme': 'base', 'themeVariables': {
  'primaryColor': '#3d2f14',
  'primaryTextColor': '#e8dcc4',
  'primaryBorderColor': '#d4af37',
  'lineColor': '#d4af37',
  'edgeLabelBackground': '#0d1b2a',
  'fontFamily': 'Georgia, serif'
}}}%%
flowchart LR
    subgraph repo["one repo"]
        direction LR
        subgraph h["heroes-service"]
            hb["build.gradle.kts<br/>applies plugin"]:::config
        end
        subgraph m["monsters-service"]
            mb["build.gradle.kts<br/>applies plugin"]:::config
        end
        subgraph e["encounters-service"]
            eb["build.gradle.kts<br/>applies plugin"]:::config
        end
        subgraph a["api-spec"]
            ab["build.gradle.kts<br/>applies plugin"]:::config
        end
        plugin["buildSrc<br/>convention plugin"]:::plugin
        plugin -.-> hb
        plugin -.-> mb
        plugin -.-> eb
        plugin -.-> ab
        h --> a
        m --> a
        e --> a
    end
    style repo fill:#0d1b2a,stroke:#d4af37,stroke-width:2px,stroke-dasharray:4 4
    style h fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style m fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style e fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    style a fill:#3d2f14,stroke:#d4af37,stroke-width:1px
    classDef config fill:#7a1f2b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
    classDef plugin fill:#1b3a4b,stroke:#d4af37,stroke-width:2px,color:#e8dcc4
```

</div>

---
transition: slide-down
---

<StageFooter icon="eye" name="The Cyclops's Cave" />

## Code Demo

<!--
TODO: content
-->

---
transition: slide-down
---

<StageFooter icon="eye" name="The Cyclops's Cave" />

## Pros & Cons

<br/>

<div class="grid grid-cols-2 gap-x-8 mt-4">
  <div>
    <h3 style="color: var(--odyssey-gold); font-weight: 700">Pros</h3>
    <ul>
      <li>Build logic deduplicated into <code>buildSrc</code> convention plugins</li>
      <li>One place to fix or evolve the Spring Boot/Kotlin conventions</li>
      <li>Modules apply a single plugin id instead of hand-rolled blocks</li>
    </ul>
  </div>
  <div>
    <h3 style="color: var(--odyssey-rose); font-weight: 700">Cons</h3>
    <ul>
      <li><code>buildSrc</code> is trapped inside this repo — no other project can reuse it</li>
      <li>Reusing it elsewhere means copy-pasting <code>buildSrc</code> itself</li>
      <li>Any change inside <code>buildSrc</code> invalidates the whole build's configuration cache</li>
    </ul>
  </div>
</div>

---
transition: slide-left
---

<StageFooter icon="eye" name="The Cyclops's Cave" />

## Conclusion

<div class="absolute inset-0 flex items-center justify-center text-center text-xl px-20">

<code>buildSrc</code> is a cave, not a harbor — nothing inside it can leave this repo.

</div>

<!--
buildSrc deduplicated the build logic beautifully — one source of truth, for this repo.
But that's exactly its limit: buildSrc is a cave, not a harbor. Nothing inside it can leave,
so any other project wanting this build logic has to copy the whole cave.
-->
