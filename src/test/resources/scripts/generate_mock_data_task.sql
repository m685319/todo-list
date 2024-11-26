-- Рекурсивный CTE для генерации последовательности от 1 до 1000
WITH RECURSIVE sequence(id) AS (
    SELECT 1 -- Начальное значение
    UNION ALL
    SELECT id + 1 FROM sequence WHERE id < 1000
)
INSERT INTO task (title, description, priority, completed, due_date, completion_date, archived)
SELECT
    'Task ' || id AS title,
    'Description for task ' || id AS description,
    MOD(id, 3) AS priority,
    CASE WHEN RAND() > 0.5 THEN TRUE ELSE FALSE END AS completed,
    CURRENT_DATE + FLOOR(RAND() * 30) AS due_date,
    CASE WHEN RAND() > 0.5 THEN CURRENT_DATE + FLOOR(RAND() * 35) ELSE NULL END AS completion_date,
    CASE WHEN RAND() > 0.7 THEN TRUE ELSE FALSE END AS archived
FROM sequence;