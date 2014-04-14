$(document).ready ->
  $.extend $.tablesorter.themes.bootstrap,
    header: 'bootstrap-header'
    sortNone: 'bootstrap-icon-unsorted'
    sortAsc: 'icon-chevron-up glyphicon glyphicon-chevron-up'
    sortDesc: 'icon-chevron-down glyphicon glyphicon-chevron-down'

  $('#ship_table').tablesorter
    sortList: [[3, 1], [4, 1]]
    theme: 'bootstrap'
    headerTemplate: '{content} {icon}'
    widgets: ["uitheme"]

  $('#modal').on 'hidden.bs.modal', ->
    $(this).removeData('bs.modal')
