chart = '#material_chart'
overview = '#material_overview'

$(document).ready ->
  userid = $('#userid').val()
  $.getJSON "/rest/v1/#{userid}/materials", (data) ->
    table = translate(data)

    option =
      xaxis: { mode: 'time' }
      yaxes: [{}, { alignTicksWithAxis: 1, position: 'right' }]
      selection: { mode: 'x' }
    plot = $.plot(chart, table, option)
    $(chart).bind 'plotselected', (event, ranges) ->
      newOpt = $.extend true, {}, option,
        xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to }
      plot = $.plot(chart, table, newOpt)

    optionO =
      series: { lines: { show: true, lineWidth: 1 }, shadowSize: 0 }
      xaxis: { ticks: [], mode: 'time' }
      yaxes: [{ ticks: [] }, { ticks: [] }]
      selection: { mode: 'x' }
    plotO = $.plot(overview, table, optionO)
    $(overview).bind('plotselected', (event, ranges) -> plot.setSelection(ranges))

translate = (data) ->
  fuel = { data: transElem(data, 'fuel'), label: '燃料', yaxis: 1 }
  ammo = { data: transElem(data, 'ammo'), label: '弾薬' , yaxis: 1 }
  steel = { data: transElem(data, 'steel'), label: '鉄鋼', yaxis: 1 }
  bauxite = { data: transElem(data, 'bauxite'), label: 'ボーキサイト', yaxis: 1 }
  instant = { data: transElem(data, 'instant'), label: '高速建造材', yaxis: 2 }
  bucket = { data: transElem(data, 'bucket'), label: '高速修復材', yaxis: 2 }
  develop = { data: transElem(data, 'develop'), label: '開発資材', yaxis: 2 }
  [fuel, ammo, steel, bauxite, instant, bucket, develop]

transElem = (data, elem) -> data.map (x) -> [x['created']/1000, x[elem]]