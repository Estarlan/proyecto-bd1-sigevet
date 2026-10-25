// Created: 18:55 24-Oct-2026
// Updated: 18:55 24-Oct-2026
// Native diagrams.net export. Usage: node diagramas/exportar_drawio.mjs [source-dir] [output-dir]
import fs from 'node:fs/promises';
import path from 'node:path';
import os from 'node:os';
import { createRequire } from 'node:module';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const require = createRequire(import.meta.url);
let chromium;
try { ({ chromium } = require('playwright')); }
catch {
  const bundled = path.join(os.homedir(), '.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
  ({ chromium } = require(process.env.PLAYWRIGHT_MODULE || bundled));
}
let executablePath = process.env.DRAWIO_CHROMIUM_PATH || chromium.executablePath();
try { await fs.access(executablePath); }
catch {
  const cache = path.join(os.homedir(), 'Library/Caches/ms-playwright');
  const versions = (await fs.readdir(cache)).filter(n => n.startsWith('chromium_headless_shell-')).sort().reverse();
  for (const version of versions) {
    for (const arch of ['x64', 'arm64']) {
      const candidate = path.join(cache, version, `chrome-headless-shell-mac-${arch}/chrome-headless-shell`);
      try { await fs.access(candidate); executablePath = candidate; break; } catch { /* next architecture */ }
    }
  }
}
const source = path.resolve(process.argv[2] || path.join(root, 'diagramas/drawio'));
const output = path.resolve(process.argv[3] || path.join(root, 'diagramas'));
const stat = await fs.stat(source);
const inputs = stat.isDirectory() ? (await fs.readdir(source)).filter(n => n.endsWith('.drawio') && !n.startsWith('SIGEVET_')).sort().map(n => path.join(source, n)) : [source];
const now = new Date().toLocaleString('en-GB', { timeZone: 'America/Bogota', day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false }).replace(/,/g, '').split(' ');
const stamp = process.env.DRAWIO_STAMP || `${now[3].replace(/:/g, '')}-${now[0]}${now[1]}${now[2]}`;
for (const ext of ['png', 'svg', 'pdf']) await fs.mkdir(path.join(output, ext), { recursive: true });
const browser = await chromium.launch({ headless: true, executablePath });
const manifest = { _created: '18:55 24-Oct-2026', renderer: 'https://app.diagrams.net/export3.html', entries: [] };
try {
  const context = await browser.newContext({ deviceScaleFactor: 2 });
  const renderer = await context.newPage();
  const capture = await context.newPage();
  for (const input of inputs) {
    const xml = await fs.readFile(input, 'utf8');
    for (let attempt = 0; ; attempt++) {
      try {
        await renderer.goto(manifest.renderer, { waitUntil: 'load', timeout: 60000 });
        await renderer.waitForFunction(() => typeof render === 'function' && typeof Graph === 'function');
        break;
      } catch (error) { if (attempt) throw error; }
    }
    await renderer.evaluate(xmlText => {
      window.exportGraph = render({ xml: xmlText, format: 'svg', from: 0, to: 0, scale: 1, border: 20, theme: 'light' });
    }, xml);
    await renderer.waitForFunction(() => document.getElementById('LoadingComplete'));
    const result = await renderer.evaluate(() => {
      const graph = window.exportGraph;
      const svg = graph.getSvg('#ffffff', 1, 20);
      return { svg: mxUtils.getXml(svg), width: parseFloat(svg.getAttribute('width')),
        height: parseFloat(svg.getAttribute('height')), cells: Object.keys(graph.model.cells).length };
    });
    if (!result.width || !result.height || result.cells < 3 || /<image\b/.test(result.svg)) throw new Error(`Empty or rasterized diagram: ${input}`);
    const stem = path.basename(input, '.drawio');
    const entry = { stem, width: result.width, height: result.height, cells: result.cells };
    for (const ext of ['svg', 'png', 'pdf']) entry[ext] = path.join(output, ext, `${stem}_${stamp}.${ext}`);
    await fs.writeFile(entry.svg, `<!-- Created: 18:55 24-Oct-2026 -->\n${result.svg}\n`);
    await capture.setViewportSize({ width: Math.ceil(result.width), height: Math.ceil(result.height) });
    await capture.setContent(`<html><head><style>@page{size:${result.width}px ${result.height}px;margin:0}html,body{margin:0;width:${result.width}px;height:${result.height}px}svg{display:block}</style></head><body>${result.svg}</body></html>`, { waitUntil: 'load' });
    await capture.evaluate(() => document.fonts.ready);
    await capture.screenshot({ path: entry.png, fullPage: true });
    await capture.pdf({ path: entry.pdf, preferCSSPageSize: true, printBackground: true });
    for (const ext of ['svg', 'png', 'pdf']) {
      if ((await fs.stat(entry[ext])).size < 500) throw new Error(`Truncated export: ${entry[ext]}`);
      entry[ext] = path.relative(root, entry[ext]);
    }
    manifest.entries.push(entry);
    console.log(`Export ${manifest.entries.length}/${inputs.length} complete: ${stem} (${result.cells} cells)`);
  }
  const manifestPath = path.join(output, 'export_manifest.json');
  try {
    const previous = JSON.parse(await fs.readFile(manifestPath, 'utf8'));
    manifest._created = previous._created || manifest._created;
    manifest._updated = [...(previous._updated || []), '18:55 24-Oct-2026'].slice(-20);
    if (!stat.isDirectory()) {
      const updated = new Set(manifest.entries.map(e => e.stem));
      manifest.entries = [...previous.entries.filter(e => !updated.has(e.stem)), ...manifest.entries].sort((a, b) => a.stem.localeCompare(b.stem));
    }
  } catch (error) { if (error.code !== 'ENOENT') throw error; }
  await fs.writeFile(manifestPath, JSON.stringify(manifest, null, 2) + '\n');
  console.log(`Verified ${manifest.entries.length}/${inputs.length} native SVG, PNG and PDF exports.`);
} finally { await browser.close(); }
