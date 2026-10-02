_open_stage_completions() {
  local cur
  cur="${COMP_WORDS[COMP_CWORD]}"
  local worktrees_dir
  worktrees_dir="$(git rev-parse --show-toplevel 2>/dev/null)/demo/.worktrees"
  [ -d "$worktrees_dir" ] || return 0
  COMPREPLY=($(compgen -W "$(ls "$worktrees_dir")" -- "$cur"))
}

complete -F _open_stage_completions open-stage.sh
complete -F _open_stage_completions stage
