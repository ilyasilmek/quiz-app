-- Initial category catalog for staging/local development.
insert into public.categories (slug, name)
values
  ('tarih', 'Tarih'),
  ('bilim', 'Bilim'),
  ('cografya', 'Coğrafya'),
  ('spor', 'Spor'),
  ('teknoloji', 'Teknoloji'),
  ('sanat', 'Sanat'),
  ('eglence', 'Eğlence'),
  ('genel-kultur', 'Genel Kültür')
on conflict (slug) do update set name = excluded.name, is_active = true;
