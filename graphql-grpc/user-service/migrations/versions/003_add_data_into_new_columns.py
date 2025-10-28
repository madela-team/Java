from alembic import op
import sqlalchemy as sa
import datetime

# Уникальный идентификатор миграции
revision = '003_add_data_into_new_columns'

# Указание на предыдущую миграцию
down_revision = '002_insert_sample_users'

branch_labels = None
depends_on = None

def upgrade():
    """
    Обновляет существующие записи в таблице `users`, устанавливая:
    - значение 'CUSTOMER' для всех строк в колонке `role`
    - текущую дату для колонки `registration_date`

    Это может быть полезно, если на предыдущем шаге в эти поля были записаны NULL
    или если миграция применялась к таблице без начальных данных.
    """
    conn = op.get_bind()
    conn.execute(
        sa.text("UPDATE users SET role = 'CUSTOMER'")
    )
    conn.execute(
        sa.text("UPDATE users SET registration_date = :today"),
        {'today': datetime.date.today()}
    )

def downgrade():
    """
    При откате миграции — удаляет все строки, где роль = 'CUSTOMER'.
    ⚠️ Это может затронуть как вручную добавленные данные, так и изначальные тестовые записи.
    Если данные ценны, нужно изменить логику отката.
    """
    op.execute("""
        DELETE FROM users WHERE role = 'CUSTOMER';
    """)
